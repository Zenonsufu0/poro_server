package kr.zenon.moncore.title;

import kr.zenon.moncore.data.PlayerProgress;
import kr.zenon.moncore.data.ZenonMonState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 서버 칭호 1차: 골드 1위(현재), 시즌 최고 부자(최고 기록), 최상위 전설 종별 최초 포획.
 */
public final class TitleService {
    public static final String WEALTH_CURRENT = "wealth_current";
    public static final String WEALTH_RECORD = "wealth_record";
    private static final String FIRST_APEX_PREFIX = "first_apex:";

    private TitleService() {}

    public static void onJoin(ServerPlayerEntity player) {
        updateKnownName(player);
        refreshCurrentRichest(player.getServer());
    }

    public static void updateWealth(ServerPlayerEntity player) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress progress = state.getOrCreate(player.getUuid());
        progress.lastKnownName = player.getGameProfile().getName();
        boolean changed = false;
        if (progress.balance > state.wealthRecordBalance) {
            state.wealthRecordUuid = player.getUuid();
            state.wealthRecordBalance = progress.balance;
            state.wealthRecordName = progress.lastKnownName;
            changed |= grant(progress, WEALTH_RECORD);
            if (progress.activeTitle == null || progress.activeTitle.isBlank()) progress.activeTitle = WEALTH_RECORD;
            player.getServer().getPlayerManager().broadcast(Text.literal("§6[칭호] §e"
                    + progress.lastKnownName + "§f 님이 §6시즌 최고 부자§f 기록을 갱신했습니다. §7("
                    + progress.balance + " 골드)"), false);
            changed = true;
        }
        changed |= refreshCurrentRichest(state);
        if (changed) state.markDirty();
    }

    public static void refreshCurrentRichest(MinecraftServer server) {
        ZenonMonState state = ZenonMonState.get(server);
        if (refreshCurrentRichest(state)) state.markDirty();
    }

    public static void awardFirstApex(ServerPlayerEntity player, String species, String displayNameKo) {
        String key = normalizeSpecies(species);
        if (key.isBlank()) return;
        ZenonMonState state = ZenonMonState.get(player.getServer());
        if (state.firstApexCatchPlayer.containsKey(key)) return;

        PlayerProgress progress = state.getOrCreate(player.getUuid());
        progress.lastKnownName = player.getGameProfile().getName();
        String titleId = FIRST_APEX_PREFIX + key;
        state.firstApexCatchPlayer.put(key, player.getUuid());
        state.firstApexCatchName.put(key, progress.lastKnownName);
        state.firstApexCatchDisplayName.put(key, safeDisplay(displayNameKo, key));
        state.firstApexCatchEpochMillis.put(key, System.currentTimeMillis());
        grant(progress, titleId);
        if (progress.activeTitle == null || progress.activeTitle.isBlank()) progress.activeTitle = titleId;
        state.markDirty();

        player.getServer().getPlayerManager().broadcast(Text.literal("§d[칭호] §e"
                + progress.lastKnownName + "§f 님이 §d" + state.firstApexCatchDisplayName.get(key)
                + "§f 최상위 전설 최초 포획 칭호를 획득했습니다."), false);
    }

    public static List<String> titleIds(ServerPlayerEntity player) {
        PlayerProgress progress = ZenonMonState.get(player.getServer()).getOrCreate(player.getUuid());
        List<String> out = new ArrayList<>(progress.titles);
        out.sort(Comparator.naturalOrder());
        return out;
    }

    public static boolean setActive(ServerPlayerEntity player, String titleId) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress progress = state.getOrCreate(player.getUuid());
        if (titleId == null || titleId.isBlank()) {
            progress.activeTitle = "";
            state.markDirty();
            return true;
        }
        if (!progress.titles.contains(titleId)) return false;
        progress.activeTitle = titleId;
        state.markDirty();
        return true;
    }

    public static String activeTitleName(ServerPlayerEntity player) {
        return activeTitleName(player.getServer(), ZenonMonState.get(player.getServer()).getOrCreate(player.getUuid()));
    }

    public static String activeTitleName(MinecraftServer server, PlayerProgress progress) {
        if (progress.activeTitle == null || progress.activeTitle.isBlank()) return "없음";
        if (!progress.titles.contains(progress.activeTitle)) return "없음";
        return displayName(server, progress.activeTitle);
    }

    public static String summary(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        List<String> ids = titleIds(player);
        if (ids.isEmpty()) return "보유 칭호 없음";
        List<String> names = new ArrayList<>();
        for (String id : ids) names.add(displayName(server, id) + " §8(" + id + ")");
        return String.join("§7, §f", names);
    }

    public static String displayName(MinecraftServer server, String titleId) {
        if (WEALTH_CURRENT.equals(titleId)) return "골드 1위";
        if (WEALTH_RECORD.equals(titleId)) return "시즌 최고 부자";
        if (titleId != null && titleId.startsWith(FIRST_APEX_PREFIX)) {
            String species = titleId.substring(FIRST_APEX_PREFIX.length());
            String display = ZenonMonState.get(server).firstApexCatchDisplayName.getOrDefault(species, species);
            return "최초 최상위: " + display;
        }
        return titleId == null || titleId.isBlank() ? "없음" : titleId;
    }

    private static void updateKnownName(ServerPlayerEntity player) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress progress = state.getOrCreate(player.getUuid());
        String name = player.getGameProfile().getName();
        if (!name.equals(progress.lastKnownName)) {
            progress.lastKnownName = name;
            state.markDirty();
        }
    }

    private static boolean refreshCurrentRichest(ZenonMonState state) {
        UUID richest = null;
        long richestBalance = 0L;
        for (Map.Entry<UUID, PlayerProgress> e : state.all().entrySet()) {
            if (e.getValue().balance > richestBalance) {
                richest = e.getKey();
                richestBalance = e.getValue().balance;
            }
        }

        boolean changed = false;
        for (Map.Entry<UUID, PlayerProgress> e : state.all().entrySet()) {
            PlayerProgress progress = e.getValue();
            boolean shouldHave = richest != null && richest.equals(e.getKey()) && richestBalance > 0L;
            if (shouldHave) {
                changed |= grant(progress, WEALTH_CURRENT);
                if (progress.activeTitle == null || progress.activeTitle.isBlank()) {
                    progress.activeTitle = WEALTH_CURRENT;
                    changed = true;
                }
            } else if (progress.titles.remove(WEALTH_CURRENT)) {
                if (WEALTH_CURRENT.equals(progress.activeTitle)) progress.activeTitle = "";
                changed = true;
            }
        }
        return changed;
    }

    private static boolean grant(PlayerProgress progress, String titleId) {
        return titleId != null && !titleId.isBlank() && progress.titles.add(titleId);
    }

    private static String normalizeSpecies(String species) {
        if (species == null) return "";
        String s = species.trim();
        int i = s.indexOf(':');
        return i >= 0 ? s.substring(i + 1) : s;
    }

    private static String safeDisplay(String displayNameKo, String fallback) {
        return displayNameKo == null || displayNameKo.isBlank() ? fallback : displayNameKo;
    }
}
