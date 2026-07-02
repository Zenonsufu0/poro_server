package kr.zenon.moncore.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import kr.zenon.moncore.ZenonMonCore;
import kr.zenon.moncore.data.PlayerProgress;
import kr.zenon.moncore.data.ZenonMonState;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** Discord 봇의 Zenon Mon 마크 제재 API 요청을 실제 서버 조치와 영속 이력으로 반영한다. */
public final class SanctionService {
    private SanctionService() {}

    public static JsonObject apply(MinecraftServer server, String action, String target,
                                   String reason, String operatorDiscordId) {
        String normalizedAction = normalizeAction(action);
        if (normalizedAction.isBlank()) return result(false, "unsupported_action");

        String rawTarget = target == null ? "" : target.trim();
        if (rawTarget.isBlank()) return result(false, "missing_target");

        ZenonMonState state = ZenonMonState.get(server);
        ResolvedTarget resolved = resolve(server, state, rawTarget);
        if (resolved == null) return result(false, "not_found");

        String cleanReason = reason == null ? "" : reason.trim();
        String operator = operatorDiscordId == null ? "" : operatorDiscordId.trim();
        BannedPlayerList banList = server.getPlayerManager().getUserBanList();

        switch (normalizedAction) {
            case "warn" -> {
                ServerPlayerEntity online = server.getPlayerManager().getPlayer(resolved.uuid());
                if (online != null) online.sendMessage(Text.literal("§e[경고] §f" + emptyReason(cleanReason)), false);
            }
            case "kick" -> {
                ServerPlayerEntity online = server.getPlayerManager().getPlayer(resolved.uuid());
                if (online == null) return result(false, "conflict");
                online.networkHandler.disconnect(Text.literal("§c[Zenon Mon] 킥: §f" + emptyReason(cleanReason)));
            }
            case "ban" -> {
                GameProfile profile = resolved.profile();
                if (banList.contains(profile)) return result(false, "conflict");
                banList.add(new BannedPlayerEntry(profile, new Date(), source(operator), null, emptyReason(cleanReason)));
                saveBanList(banList);
                ServerPlayerEntity online = server.getPlayerManager().getPlayer(resolved.uuid());
                if (online != null) {
                    online.networkHandler.disconnect(Text.literal("§c[Zenon Mon] 밴: §f" + emptyReason(cleanReason)));
                }
            }
            case "unban" -> {
                GameProfile profile = resolved.profile();
                if (!banList.contains(profile)) return result(false, "conflict");
                banList.remove(profile);
                saveBanList(banList);
            }
            default -> {
                return result(false, "unsupported_action");
            }
        }

        ZenonMonState.SanctionRecord record = new ZenonMonState.SanctionRecord(
                state.nextSanctionId++,
                System.currentTimeMillis(),
                normalizedAction,
                resolved.uuid(),
                resolved.name(),
                rawTarget,
                operator,
                cleanReason,
                "discord");
        state.sanctions.add(record);
        state.markDirty();
        ZenonMonCore.LOGGER.info("[Sanction] {} target={} uuid={} operator={} reason={}",
                normalizedAction, resolved.name(), resolved.uuid(), operator, cleanReason);

        JsonObject out = result(true, "");
        addRecordFields(out, record);
        out.addProperty("id", record.id());
        out.addProperty("player", resolved.name());
        out.addProperty("uuid", resolved.uuid().toString());
        return out;
    }

    public static JsonObject list(MinecraftServer server, String target) {
        String rawTarget = target == null ? "" : target.trim();
        if (rawTarget.isBlank()) return result(false, "missing_target");

        ZenonMonState state = ZenonMonState.get(server);
        ResolvedTarget resolved = resolve(server, state, rawTarget);
        JsonArray rows = new JsonArray();
        for (ZenonMonState.SanctionRecord record : state.sanctions) {
            if (matches(record, rawTarget, resolved)) {
                JsonObject row = new JsonObject();
                addRecordFields(row, record);
                rows.add(row);
            }
        }

        if (resolved == null && rows.isEmpty()) return result(false, "not_found");
        JsonObject out = result(true, "");
        out.add("sanctions", rows);
        if (resolved != null) {
            out.addProperty("player", resolved.name());
            out.addProperty("uuid", resolved.uuid().toString());
        }
        return out;
    }

    private static ResolvedTarget resolve(MinecraftServer server, ZenonMonState state, String target) {
        ServerPlayerEntity onlineByName = server.getPlayerManager().getPlayer(target);
        if (onlineByName != null) return fromOnline(onlineByName);

        try {
            UUID uuid = UUID.fromString(target);
            ServerPlayerEntity online = server.getPlayerManager().getPlayer(uuid);
            if (online != null) return fromOnline(online);
            PlayerProgress progress = state.peek(uuid);
            String name = progress != null && progress.lastKnownName != null && !progress.lastKnownName.isBlank()
                    ? progress.lastKnownName : target;
            return new ResolvedTarget(uuid, name, new GameProfile(uuid, name));
        } catch (IllegalArgumentException ignored) {
            // UUID가 아니면 닉네임/디스코드 ID로 계속 탐색.
        }

        String lower = target.toLowerCase(Locale.ROOT);
        for (Map.Entry<UUID, PlayerProgress> e : state.all().entrySet()) {
            PlayerProgress progress = e.getValue();
            if (equalsIgnoreCase(progress.lastKnownName, target) || target.equals(progress.discordId)) {
                String name = progress.lastKnownName == null || progress.lastKnownName.isBlank()
                        ? e.getKey().toString() : progress.lastKnownName;
                return new ResolvedTarget(e.getKey(), name, new GameProfile(e.getKey(), name));
            }
        }

        return server.getUserCache().findByName(target)
                .map(profile -> new ResolvedTarget(profile.getId(), profile.getName(), profile))
                .orElse(null);
    }

    private static ResolvedTarget fromOnline(ServerPlayerEntity player) {
        return new ResolvedTarget(player.getUuid(), player.getGameProfile().getName(), player.getGameProfile());
    }

    private static boolean matches(ZenonMonState.SanctionRecord record, String target, ResolvedTarget resolved) {
        if (resolved != null && resolved.uuid().equals(record.playerUuid())) return true;
        return equalsIgnoreCase(record.target(), target)
                || equalsIgnoreCase(record.playerName(), target)
                || (record.playerUuid() != null && target.equals(record.playerUuid().toString()));
    }

    private static void addRecordFields(JsonObject out, ZenonMonState.SanctionRecord record) {
        out.addProperty("id", record.id());
        out.addProperty("action", record.action());
        out.addProperty("createdAt", record.epochMillis());
        out.addProperty("player", record.playerName());
        out.addProperty("name", record.playerName());
        out.addProperty("uuid", record.playerUuid() == null ? "" : record.playerUuid().toString());
        out.addProperty("target", record.target());
        out.addProperty("operatorDiscordId", record.operatorDiscordId());
        out.addProperty("reason", record.reason());
        out.addProperty("source", record.source());
    }

    private static JsonObject result(boolean ok, String reason) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", ok);
        if (!ok && reason != null && !reason.isBlank()) out.addProperty("reason", reason);
        return out;
    }

    private static String normalizeAction(String action) {
        if (action == null) return "";
        return switch (action.trim().toLowerCase(Locale.ROOT)) {
            case "warn", "kick", "ban", "unban" -> action.trim().toLowerCase(Locale.ROOT);
            default -> "";
        };
    }

    private static String emptyReason(String reason) {
        return reason == null || reason.isBlank() ? "사유 없음" : reason;
    }

    private static String source(String operatorDiscordId) {
        return operatorDiscordId == null || operatorDiscordId.isBlank()
                ? "Zenon Discord" : "Zenon Discord " + operatorDiscordId;
    }

    private static boolean equalsIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

    private static void saveBanList(BannedPlayerList banList) {
        try {
            banList.save();
        } catch (IOException e) {
            ZenonMonCore.LOGGER.warn("[Sanction] banned-players.json 저장 실패", e);
        }
    }

    private record ResolvedTarget(UUID uuid, String name, GameProfile profile) {}
}
