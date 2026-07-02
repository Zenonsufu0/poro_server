package kr.zenon.moncore.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kr.zenon.moncore.ZenonMonCore;
import kr.zenon.moncore.config.ConfigManager;
import kr.zenon.moncore.config.CoreConfig;
import kr.zenon.moncore.economy.EconomyReportService;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 디스코드 봇용 인증 HTTP API (결정 041, JDK 내장 HttpServer — 무의존).
 *  - POST /auth/verify  헤더 X-API-Key, 바디 {"code","discordId"} → 코드 검증 → {"ok","uuid"}.
 *  - GET  /auth/ping    헬스체크.
 * 봇(integrations/zenonmon_api.py)이 API 키로 호출. 검증 로직은 AuthManager(서버 스레드 위임).
 */
public final class AuthHttpServer {
    private AuthHttpServer() {}

    private static final Gson GSON = new Gson();
    private static HttpServer http;
    private static volatile MinecraftServer server;

    public static void start(MinecraftServer minecraftServer) {
        CoreConfig.DiscordAuth cfg = ConfigManager.core().discordAuth;
        if (!cfg.enabled) return;
        stop();
        server = minecraftServer;
        try {
            http = HttpServer.create(new InetSocketAddress(cfg.bindAddress, cfg.httpPort), 0);
            http.setExecutor(Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "Zenon Mon-AuthHttp");
                t.setDaemon(true);
                return t;
            }));
            http.createContext("/auth/verify", AuthHttpServer::handleVerify);
            http.createContext("/auth/ping", AuthHttpServer::handlePing);
            http.createContext("/economy/summary", AuthHttpServer::handleEconomySummary);
            http.createContext("/economy/alerts", AuthHttpServer::handleEconomyAlerts);
            http.createContext("/economy/dashboard", AuthHttpServer::handleEconomyDashboard);
            http.start();
            ZenonMonCore.LOGGER.info("[Auth] 인증 HTTP API 시작: {}:{}", cfg.bindAddress, cfg.httpPort);
            if ("CHANGE_ME".equals(cfg.apiKey)) {
                ZenonMonCore.LOGGER.warn("[Auth] ⚠️ apiKey가 기본값(CHANGE_ME) — core.json에서 반드시 변경하세요.");
            }
        } catch (IOException e) {
            ZenonMonCore.LOGGER.error("[Auth] HTTP API 시작 실패 ({}:{})", cfg.bindAddress, cfg.httpPort, e);
        }
    }

    public static void stop() {
        if (http != null) {
            http.stop(0);
            http = null;
        }
        server = null;
    }

    private static void handlePing(HttpExchange ex) throws IOException {
        respond(ex, 200, "{\"ok\":true,\"service\":\"zenonmoncore-auth\"}");
    }

    private static void handleVerify(HttpExchange ex) throws IOException {
        try {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { respond(ex, 405, err("method")); return; }
            CoreConfig.DiscordAuth cfg = ConfigManager.core().discordAuth;
            if (!authorized(ex, cfg)) { respond(ex, 401, err("unauthorized")); return; }

            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = GSON.fromJson(body, JsonObject.class);
            String code = json != null && json.has("code") ? json.get("code").getAsString() : null;
            String discordId = json != null && json.has("discordId") ? json.get("discordId").getAsString() : null;
            if (code == null || discordId == null) { respond(ex, 400, err("missing code/discordId")); return; }

            UUID uuid = AuthManager.verify(code, discordId);
            if (uuid == null) { respond(ex, 404, err("invalid or expired code")); return; }
            respond(ex, 200, "{\"ok\":true,\"uuid\":\"" + uuid + "\"}");
        } catch (Exception e) {
            ZenonMonCore.LOGGER.error("[Auth] /auth/verify 처리 오류", e);
            try { respond(ex, 500, err("server error")); } catch (IOException ignored) {}
        }
    }

    private static String err(String msg) { return "{\"ok\":false,\"error\":\"" + msg + "\"}"; }

    private static void handleEconomySummary(HttpExchange ex) throws IOException {
        if (!economyHttpAllowed(ex)) return;
        try {
            JsonObject json = onServerThread(() -> EconomyReportService.summary(server));
            respond(ex, 200, GSON.toJson(json));
        } catch (Exception e) {
            ZenonMonCore.LOGGER.error("[EconomyHTTP] /economy/summary 처리 오류", e);
            respond(ex, 500, err("server error"));
        }
    }

    private static void handleEconomyAlerts(HttpExchange ex) throws IOException {
        if (!economyHttpAllowed(ex)) return;
        try {
            JsonObject root = onServerThread(() -> {
                JsonObject json = new JsonObject();
                json.addProperty("ok", true);
                json.addProperty("generatedAt", System.currentTimeMillis());
                json.add("alerts", EconomyReportService.summary(server).getAsJsonArray("alerts"));
                return json;
            });
            respond(ex, 200, GSON.toJson(root));
        } catch (Exception e) {
            ZenonMonCore.LOGGER.error("[EconomyHTTP] /economy/alerts 처리 오류", e);
            respond(ex, 500, err("server error"));
        }
    }

    private static void handleEconomyDashboard(HttpExchange ex) throws IOException {
        if (!economyHttpAllowed(ex)) return;
        respondHtml(ex, 200, dashboardHtml());
    }

    private static boolean economyHttpAllowed(HttpExchange ex) throws IOException {
        if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) { respond(ex, 405, err("method")); return false; }
        CoreConfig cfg = ConfigManager.core();
        if (!cfg.economyMonitor.httpEnabled) { respond(ex, 404, err("disabled")); return false; }
        if (!authorized(ex, cfg.discordAuth)) { respond(ex, 401, err("unauthorized")); return false; }
        if (server == null) { respond(ex, 503, err("server unavailable")); return false; }
        return true;
    }

    private static boolean authorized(HttpExchange ex, CoreConfig.DiscordAuth cfg) {
        String key = ex.getRequestHeaders().getFirst("X-API-Key");
        if (key == null || key.isBlank()) key = queryParam(ex, "key");
        return key != null && key.equals(cfg.apiKey);
    }

    private static String queryParam(HttpExchange ex, String name) {
        String query = ex.getRequestURI().getRawQuery();
        if (query == null || query.isBlank()) return null;
        for (String part : query.split("&")) {
            int i = part.indexOf('=');
            String k = i >= 0 ? part.substring(0, i) : part;
            if (!name.equals(urlDecode(k))) continue;
            return i >= 0 ? urlDecode(part.substring(i + 1)) : "";
        }
        return null;
    }

    private static String urlDecode(String raw) {
        return URLDecoder.decode(raw, StandardCharsets.UTF_8);
    }

    private interface ServerTask<T> { T run(); }

    private static <T> T onServerThread(ServerTask<T> task) throws Exception {
        MinecraftServer s = server;
        if (s == null) throw new IllegalStateException("server unavailable");
        CompletableFuture<T> future = new CompletableFuture<>();
        s.execute(() -> {
            try {
                future.complete(task.run());
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future.get(5, TimeUnit.SECONDS);
    }

    private static void respond(HttpExchange ex, int status, String json) throws IOException {
        byte[] b = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
    }

    private static void respondHtml(HttpExchange ex, int status, String html) throws IOException {
        byte[] b = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(status, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
    }

    private static String dashboardHtml() {
        return """
                <!doctype html>
                <html lang="ko">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Zenon Mon Economy</title>
                  <style>
                    body{margin:0;background:#111;color:#e8e8e8;font-family:system-ui,-apple-system,Segoe UI,sans-serif}
                    header{padding:16px 20px;background:#191919;border-bottom:1px solid #303030;display:flex;justify-content:space-between;gap:16px;align-items:center}
                    main{padding:18px;display:grid;gap:16px}
                    .grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:12px}
                    section{border:1px solid #303030;border-radius:8px;padding:14px;background:#171717}
                    h1{font-size:20px;margin:0} h2{font-size:15px;margin:0 0 10px}
                    .metric{font-size:26px;font-weight:700}.muted{color:#aaa;font-size:12px}
                    table{width:100%;border-collapse:collapse;font-size:13px}td,th{padding:6px;border-bottom:1px solid #2b2b2b;text-align:left}th{color:#bbb}
                    .pos{color:#62d26f}.neg{color:#ff7777}.warn{color:#ffd166}
                  </style>
                </head>
                <body>
                  <header><h1>Zenon Mon Economy</h1><div class="muted" id="updated">loading</div></header>
                  <main>
                    <div class="grid" id="metrics"></div>
                    <div class="grid">
                      <section><h2>유저 보유 상위</h2><table id="players"></table></section>
                      <section><h2>판매 품목 생산 상위</h2><table id="sellItems"></table></section>
                      <section><h2>소모처 상위</h2><table id="sinks"></table></section>
                    </div>
                    <section><h2>최근 거래</h2><table id="recent"></table></section>
                    <section><h2>경고</h2><table id="alerts"></table></section>
                  </main>
                  <script>
                    const key = new URLSearchParams(location.search).get('key') || '';
                    const money = n => Number(n||0).toLocaleString('ko-KR');
                    const row = cells => '<tr>'+cells.map(c=>'<td>'+c+'</td>').join('')+'</tr>';
                    function fillTable(id, head, rows){ document.getElementById(id).innerHTML='<tr>'+head.map(h=>'<th>'+h+'</th>').join('')+'</tr>'+rows.join(''); }
                    async function load(){
                      const res = await fetch('/economy/summary?key='+encodeURIComponent(key));
                      const d = await res.json();
                      document.getElementById('updated').textContent = new Date(d.generatedAt).toLocaleString('ko-KR');
                      document.getElementById('metrics').innerHTML =
                        [['현재 보유 총량',d.totalBalance],['누적 생산량',d.totalProduced],['누적 소모량',d.totalConsumed],['순증',d.netCreated]]
                        .map(([k,v])=>`<section><div class="muted">${k}</div><div class="metric">${money(v)}</div></section>`).join('');
                      fillTable('players',['이름','잔액','생산','소모'], d.topPlayers.map(p=>row([p.name,money(p.balance),money(p.produced),money(p.consumed)])));
                      fillTable('sellItems',['품목','생산 골드','수량'], d.topSellItems.map(x=>row([x.key,money(x.gold),money(x.count)])));
                      fillTable('sinks',['소모처','골드'], d.topSinks.map(x=>row([x.key,money(x.gold)])));
                      fillTable('recent',['유저','변동','잔액','source','item'], d.recentTransactions.map(t=>row([t.name,`<span class="${t.delta>=0?'pos':'neg'}">${money(t.delta)}</span>`,money(t.balanceAfter),t.source,t.itemId||''])));
                      fillTable('alerts',['종류','유저','값','내용'], d.alerts.map(a=>row([`<span class="warn">${a.type}</span>`,a.name,money(a.value),a.message])));
                    }
                    load(); setInterval(load, 30000);
                  </script>
                </body>
                </html>
                """;
    }
}
