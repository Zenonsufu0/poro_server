package kr.zenon.moncore.economy;

import com.google.gson.JsonObject;
import kr.zenon.moncore.ZenonMonCore;
import kr.zenon.moncore.config.ConfigManager;
import kr.zenon.moncore.config.CoreConfig;
import net.minecraft.server.MinecraftServer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/** 경제 요약을 디스코드 웹훅으로 주기 전송한다. 봇 연동 전에도 운영 채널 알림용으로 사용 가능. */
public final class EconomyDiscordNotifier {
    private EconomyDiscordNotifier() {}

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static long nextSendTick = 0L;

    public static void tick(MinecraftServer server) {
        CoreConfig.EconomyMonitor cfg = ConfigManager.core().economyMonitor;
        if (!cfg.discordWebhookEnabled || cfg.discordWebhookUrl == null || cfg.discordWebhookUrl.isBlank()) return;
        long tick = server.getTicks();
        if (tick < nextSendTick) return;
        int intervalTicks = Math.max(1, cfg.discordIntervalMinutes) * 60 * 20;
        nextSendTick = tick + intervalTicks;
        send(server, cfg.discordWebhookUrl);
    }

    private static void send(MinecraftServer server, String webhookUrl) {
        try {
            String content = EconomyReportService.discordSummary(server);
            JsonObject body = new JsonObject();
            body.addProperty("username", "Zenon Mon Economy");
            body.addProperty("content", content);
            HttpRequest request = HttpRequest.newBuilder(URI.create(webhookUrl))
                    .header("Content-Type", "application/json; charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                    .build();
            HTTP.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .thenAccept(res -> {
                        if (res.statusCode() < 200 || res.statusCode() >= 300) {
                            ZenonMonCore.LOGGER.warn("[EconomyDiscord] 웹훅 전송 실패 status={}", res.statusCode());
                        }
                    })
                    .exceptionally(t -> {
                        ZenonMonCore.LOGGER.warn("[EconomyDiscord] 웹훅 전송 오류: {}", t.toString());
                        return null;
                    });
        } catch (Exception e) {
            ZenonMonCore.LOGGER.warn("[EconomyDiscord] 웹훅 전송 준비 실패: {}", e.toString());
        }
    }
}
