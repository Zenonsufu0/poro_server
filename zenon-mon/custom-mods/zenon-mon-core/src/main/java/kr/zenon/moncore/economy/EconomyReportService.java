package kr.zenon.moncore.economy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kr.zenon.moncore.config.ConfigManager;
import kr.zenon.moncore.config.CoreConfig;
import kr.zenon.moncore.data.PlayerProgress;
import kr.zenon.moncore.data.ZenonMonState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** 경제 운영 모니터용 JSON 스냅샷 생성기. HTTP API와 디스코드 알림이 공유한다. */
public final class EconomyReportService {
    private EconomyReportService() {}

    public static JsonObject summary(MinecraftServer server) {
        ZenonMonState s = ZenonMonState.get(server);
        int limit = Math.max(1, ConfigManager.core().economyMonitor.topLimit);

        long totalBalance = s.all().values().stream().mapToLong(p -> p.balance).sum();
        long totalIn = s.goldIn.values().stream().mapToLong(Long::longValue).sum();
        long totalOut = s.goldOut.values().stream().mapToLong(Long::longValue).sum();

        JsonObject root = new JsonObject();
        root.addProperty("ok", true);
        root.addProperty("generatedAt", System.currentTimeMillis());
        root.addProperty("playerCount", s.all().size());
        root.addProperty("totalBalance", totalBalance);
        root.addProperty("totalProduced", totalIn);
        root.addProperty("totalConsumed", totalOut);
        root.addProperty("netCreated", totalIn - totalOut);
        root.add("groups", groups(s));
        root.add("topPlayers", topPlayers(server, s, limit));
        root.add("topSellItems", topMap(s.itemSellGold, s.itemSellCount, "gold", "count", limit));
        root.add("topBuyItems", topMap(s.itemBuyGold, s.itemBuyCount, "gold", "count", limit));
        root.add("topSinks", topMap(s.goldOutBySource, null, "gold", "count", limit));
        root.add("recentTransactions", recent(s, limit));
        root.add("alerts", alerts(server, s, limit));
        return root;
    }

    public static JsonArray alerts(MinecraftServer server, ZenonMonState s, int limit) {
        JsonArray out = new JsonArray();
        CoreConfig.EconomyMonitor cfg = ConfigManager.core().economyMonitor;
        long balanceLimit = cfg.suspiciousBalanceThreshold;
        long netGainLimit = cfg.suspiciousNetGainThreshold;
        long singleTxLimit = cfg.suspiciousSingleTransactionThreshold;

        List<Map.Entry<UUID, PlayerProgress>> players = new ArrayList<>(s.all().entrySet());
        players.sort(Comparator.<Map.Entry<UUID, PlayerProgress>>comparingLong(e -> e.getValue().balance).reversed());
        int emitted = 0;
        double averageBalance = averageBalance(s);
        for (Map.Entry<UUID, PlayerProgress> e : players) {
            UUID uuid = e.getKey();
            PlayerProgress p = e.getValue();
            long in = s.playerGoldIn.getOrDefault(uuid, 0L);
            long outGold = s.playerGoldOut.getOrDefault(uuid, 0L);
            long net = in - outGold;
            if (p.balance >= balanceLimit) {
                out.add(alert("high_balance", playerName(server, uuid, p), uuid, p.balance,
                        "현재 잔액 " + p.balance));
                if (++emitted >= limit) break;
            }
            if (s.all().size() >= cfg.suspiciousBalanceMinPlayers
                    && averageBalance > 0
                    && p.balance >= Math.round(averageBalance * cfg.suspiciousBalanceAverageMultiplier)) {
                out.add(alert("balance_outlier", playerName(server, uuid, p), uuid, p.balance,
                        "평균 잔액 " + Math.round(averageBalance) + " 대비 "
                                + String.format("%.1f", p.balance / averageBalance) + "배"));
                if (++emitted >= limit) break;
            }
            if (net >= netGainLimit) {
                out.add(alert("high_net_gain", playerName(server, uuid, p), uuid, net,
                        "누적 순생산 " + net + " (생산 " + in + " / 소모 " + outGold + ")"));
                if (++emitted >= limit) break;
            }
        }

        emitted = addSellItemAlerts(out, s, emitted, limit, cfg);
        emitted = addTicketAlerts(out, server, s, emitted, limit, cfg);

        List<ZenonMonState.EconomyTransaction> txs = s.economyTransactions;
        for (int i = txs.size() - 1; i >= 0 && emitted < limit; i--) {
            ZenonMonState.EconomyTransaction tx = txs.get(i);
            if (Math.abs(tx.delta()) >= singleTxLimit) {
                out.add(alert("large_transaction", tx.playerName(), tx.playerUuid(), tx.delta(),
                        "단일 거래 " + tx.delta() + " / " + tx.source()));
                emitted++;
            }
        }
        return out;
    }

    private static int addSellItemAlerts(JsonArray out, ZenonMonState s, int emitted, int limit,
                                         CoreConfig.EconomyMonitor cfg) {
        if (emitted >= limit || s.itemSellGold.isEmpty()) return emitted;
        long totalSellGold = s.itemSellGold.values().stream().mapToLong(Long::longValue).sum();
        double average = totalSellGold / (double) Math.max(1, s.itemSellGold.size());
        List<Map.Entry<String, Long>> items = new ArrayList<>(s.itemSellGold.entrySet());
        items.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        for (Map.Entry<String, Long> e : items) {
            if (emitted >= limit) break;
            long gold = e.getValue();
            if (gold < cfg.suspiciousSellItemGoldThreshold) continue;
            double share = totalSellGold <= 0 ? 0.0 : gold * 100.0 / totalSellGold;
            double multiplier = average <= 0 ? 0.0 : gold / average;
            if (share >= cfg.suspiciousSellItemSharePercent
                    || multiplier >= cfg.suspiciousSellItemAverageMultiplier) {
                out.add(alert("sell_item_outlier", e.getKey(), new UUID(0L, 0L), gold,
                        "판매 생산 " + gold + " / 전체 판매 " + String.format("%.1f", share)
                                + "% / 평균 대비 " + String.format("%.1f", multiplier) + "배"));
                emitted++;
            }
        }
        return emitted;
    }

    private static int addTicketAlerts(JsonArray out, MinecraftServer server, ZenonMonState s, int emitted, int limit,
                                       CoreConfig.EconomyMonitor cfg) {
        if (emitted >= limit) return emitted;
        long cutoff = System.currentTimeMillis() - Math.max(1, cfg.suspiciousTicketWindowHours) * 60L * 60L * 1000L;
        Map<UUID, TicketWindow> byPlayer = new java.util.HashMap<>();
        for (ZenonMonState.EconomyTransaction tx : s.economyTransactions) {
            if (tx.epochMillis() < cutoff || !tx.source().startsWith("ticket:") || tx.delta() >= 0) continue;
            TicketWindow w = byPlayer.computeIfAbsent(tx.playerUuid(), uuid -> new TicketWindow(tx.playerName()));
            w.count++;
            w.gold += -tx.delta();
        }
        List<Map.Entry<UUID, TicketWindow>> rows = new ArrayList<>(byPlayer.entrySet());
        rows.sort(Comparator.<Map.Entry<UUID, TicketWindow>>comparingLong(e -> e.getValue().gold).reversed());
        for (Map.Entry<UUID, TicketWindow> e : rows) {
            if (emitted >= limit) break;
            TicketWindow w = e.getValue();
            if (w.count >= cfg.suspiciousTicketPurchaseCount || w.gold >= cfg.suspiciousTicketPurchaseGold) {
                out.add(alert("ticket_purchase_spike", w.name, e.getKey(), w.gold,
                        "최근 " + cfg.suspiciousTicketWindowHours + "시간 전설 티켓 "
                                + w.count + "회 / " + w.gold + " 골드"));
                emitted++;
            }
        }
        return emitted;
    }

    private static double averageBalance(ZenonMonState s) {
        if (s.all().isEmpty()) return 0.0;
        return s.all().values().stream().mapToLong(p -> p.balance).average().orElse(0.0);
    }

    private static final class TicketWindow {
        private final String name;
        private int count;
        private long gold;

        private TicketWindow(String name) {
            this.name = name == null || name.isBlank() ? "unknown" : name;
        }
    }

    public static String discordSummary(MinecraftServer server) {
        JsonObject s = summary(server);
        StringBuilder sb = new StringBuilder();
        sb.append("**Zenon Mon 경제 요약**\\n");
        sb.append("- 현재 보유 총량: `").append(s.get("totalBalance").getAsLong()).append("`\\n");
        sb.append("- 누적 생산/소모: `").append(s.get("totalProduced").getAsLong())
                .append("` / `").append(s.get("totalConsumed").getAsLong()).append("`\\n");
        sb.append("- 순증: `").append(s.get("netCreated").getAsLong()).append("`\\n");
        appendTop(sb, "보유 상위", s.getAsJsonArray("topPlayers"), "name", "balance");
        appendTop(sb, "생산 품목 상위", s.getAsJsonArray("topSellItems"), "key", "gold");
        JsonArray alerts = s.getAsJsonArray("alerts");
        if (!alerts.isEmpty()) {
            sb.append("- 경고: `").append(alerts.size()).append("건`\\n");
            for (int i = 0; i < Math.min(3, alerts.size()); i++) {
                JsonObject a = alerts.get(i).getAsJsonObject();
                sb.append("  - ").append(a.get("type").getAsString()).append(": ")
                        .append(a.get("name").getAsString()).append(" / ")
                        .append(a.get("message").getAsString()).append("\\n");
            }
        }
        return sb.toString();
    }

    private static JsonArray groups(ZenonMonState s) {
        JsonArray out = new JsonArray();
        List<String> keys = new ArrayList<>();
        keys.addAll(s.goldIn.keySet());
        for (String k : s.goldOut.keySet()) if (!keys.contains(k)) keys.add(k);
        keys.sort(String::compareTo);
        for (String k : keys) {
            JsonObject o = new JsonObject();
            long in = s.goldIn.getOrDefault(k, 0L);
            long outGold = s.goldOut.getOrDefault(k, 0L);
            o.addProperty("key", k);
            o.addProperty("in", in);
            o.addProperty("out", outGold);
            o.addProperty("net", in - outGold);
            out.add(o);
        }
        return out;
    }

    private static JsonArray topPlayers(MinecraftServer server, ZenonMonState s, int limit) {
        List<Map.Entry<UUID, PlayerProgress>> list = new ArrayList<>(s.all().entrySet());
        list.sort(Comparator.<Map.Entry<UUID, PlayerProgress>>comparingLong(e -> e.getValue().balance).reversed());
        JsonArray out = new JsonArray();
        for (int i = 0; i < Math.min(limit, list.size()); i++) {
            UUID uuid = list.get(i).getKey();
            PlayerProgress p = list.get(i).getValue();
            JsonObject o = new JsonObject();
            o.addProperty("uuid", uuid.toString());
            o.addProperty("name", playerName(server, uuid, p));
            o.addProperty("balance", p.balance);
            o.addProperty("produced", s.playerGoldIn.getOrDefault(uuid, 0L));
            o.addProperty("consumed", s.playerGoldOut.getOrDefault(uuid, 0L));
            o.addProperty("net", s.playerGoldIn.getOrDefault(uuid, 0L) - s.playerGoldOut.getOrDefault(uuid, 0L));
            out.add(o);
        }
        return out;
    }

    private static JsonArray topMap(Map<String, Long> value, Map<String, Long> count,
                                    String valueName, String countName, int limit) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(value.entrySet());
        list.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        JsonArray out = new JsonArray();
        for (int i = 0; i < Math.min(limit, list.size()); i++) {
            Map.Entry<String, Long> e = list.get(i);
            JsonObject o = new JsonObject();
            o.addProperty("key", e.getKey());
            o.addProperty(valueName, e.getValue());
            if (count != null) o.addProperty(countName, count.getOrDefault(e.getKey(), 0L));
            out.add(o);
        }
        return out;
    }

    private static JsonArray recent(ZenonMonState s, int limit) {
        JsonArray out = new JsonArray();
        List<ZenonMonState.EconomyTransaction> txs = s.economyTransactions;
        for (int i = txs.size() - 1; i >= 0 && out.size() < limit; i--) {
            ZenonMonState.EconomyTransaction tx = txs.get(i);
            JsonObject o = new JsonObject();
            o.addProperty("epochMillis", tx.epochMillis());
            o.addProperty("uuid", tx.playerUuid().toString());
            o.addProperty("name", tx.playerName());
            o.addProperty("delta", tx.delta());
            o.addProperty("balanceAfter", tx.balanceAfter());
            o.addProperty("source", tx.source());
            o.addProperty("itemId", tx.itemId());
            o.addProperty("count", tx.count());
            out.add(o);
        }
        return out;
    }

    private static JsonObject alert(String type, String name, UUID uuid, long value, String message) {
        JsonObject o = new JsonObject();
        o.addProperty("type", type);
        o.addProperty("name", name);
        o.addProperty("uuid", uuid.toString());
        o.addProperty("value", value);
        o.addProperty("message", message);
        return o;
    }

    private static String playerName(MinecraftServer server, UUID uuid, PlayerProgress p) {
        ServerPlayerEntity online = server.getPlayerManager().getPlayer(uuid);
        if (online != null) return online.getGameProfile().getName();
        if (p.lastKnownName != null && !p.lastKnownName.isBlank()) return p.lastKnownName;
        String s = uuid.toString();
        return s.substring(0, Math.min(8, s.length()));
    }

    private static void appendTop(StringBuilder sb, String title, JsonArray rows, String keyName, String valueName) {
        if (rows.isEmpty()) return;
        sb.append("- ").append(title).append(": ");
        int n = Math.min(3, rows.size());
        for (int i = 0; i < n; i++) {
            JsonObject o = rows.get(i).getAsJsonObject();
            if (i > 0) sb.append(", ");
            sb.append(o.get(keyName).getAsString()).append(" `").append(o.get(valueName).getAsLong()).append("`");
        }
        sb.append("\\n");
    }
}
