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
 * 서버 칭호 1차: 큰손(누적 소비), 부자(현재 보유 1위), 최상위 전설 종별 최초 포획.
 */
public final class TitleService {
    public static final String WEALTH_CURRENT = "wealth_current";
    public static final String WEALTH_RECORD = "wealth_record";
    public static final String MILLION_SPENDER = "million_spender";
    private static final String FIRST_APEX_PREFIX = "first_apex:";
    private static final long WEALTH_THRESHOLD = 1_000_000L;
    private static final long SPEND_THRESHOLD = 1_000_000L;
    private static final long RICHEST_REFRESH_TICKS = 60L * 60L * 20L;

    private static long nextRichestRefreshTick = 0L;

    private TitleService() {}

    public static void onJoin(ServerPlayerEntity player) {
        updateKnownName(player);
        updateSpending(player);
    }

    public static void updateWealth(ServerPlayerEntity player) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress progress = state.getOrCreate(player.getUuid());
        progress.lastKnownName = player.getGameProfile().getName();
        boolean changed = false;
        if (progress.balance >= WEALTH_THRESHOLD && progress.balance > state.wealthRecordBalance) {
            state.wealthRecordUuid = player.getUuid();
            state.wealthRecordBalance = progress.balance;
            state.wealthRecordName = progress.lastKnownName;
            changed = true;
        }
        changed |= updateSpending(state, player, progress);
        if (changed) state.markDirty();
    }

    public static void tick(MinecraftServer server) {
        long now = server.getTicks();
        if (now < nextRichestRefreshTick) return;
        nextRichestRefreshTick = now + RICHEST_REFRESH_TICKS;
        refreshCurrentRichest(server);
    }

    private static void refreshCurrentRichest(MinecraftServer server) {
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
        if (WEALTH_CURRENT.equals(titleId)) return "부자";
        if (WEALTH_RECORD.equals(titleId)) return "부자";
        if (MILLION_SPENDER.equals(titleId)) return "큰손";
        if (titleId != null && titleId.startsWith(FIRST_APEX_PREFIX)) {
            String species = titleId.substring(FIRST_APEX_PREFIX.length());
            return apexTitleName(species,
                    ZenonMonState.get(server).firstApexCatchDisplayName.getOrDefault(species, species));
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
            long balance = e.getValue().balance;
            if (balance < WEALTH_THRESHOLD) continue;
            if (balance > richestBalance || (balance == richestBalance && prefer(e.getKey(), richest))) {
                richest = e.getKey();
                richestBalance = balance;
            }
        }

        boolean changed = false;
        for (Map.Entry<UUID, PlayerProgress> e : state.all().entrySet()) {
            PlayerProgress progress = e.getValue();
            if (progress.titles.remove(WEALTH_RECORD)) {
                if (WEALTH_RECORD.equals(progress.activeTitle)) progress.activeTitle = "";
                changed = true;
            }
            boolean shouldHave = richest != null && richest.equals(e.getKey());
            if (shouldHave) {
                boolean granted = grant(progress, WEALTH_CURRENT);
                changed |= granted;
                if (granted && (progress.activeTitle == null || progress.activeTitle.isBlank())) {
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

    private static boolean updateSpending(ServerPlayerEntity player) {
        ZenonMonState state = ZenonMonState.get(player.getServer());
        PlayerProgress progress = state.getOrCreate(player.getUuid());
        if (updateSpending(state, player, progress)) {
            state.markDirty();
            return true;
        }
        return false;
    }

    private static boolean updateSpending(ZenonMonState state, ServerPlayerEntity player, PlayerProgress progress) {
        long spent = state.playerGoldOut.getOrDefault(player.getUuid(), 0L);
        if (spent < SPEND_THRESHOLD) return false;
        boolean granted = grant(progress, MILLION_SPENDER);
        if (granted && (progress.activeTitle == null || progress.activeTitle.isBlank())) {
            progress.activeTitle = MILLION_SPENDER;
        }
        return granted;
    }

    private static boolean prefer(UUID candidate, UUID current) {
        return current == null || candidate.toString().compareTo(current.toString()) < 0;
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

    private static String apexTitleName(String species, String fallbackDisplay) {
        return switch (species) {
            case "rayquaza" -> "하늘의 개척자";
            case "kyogre" -> "심해의 지배자";
            case "groudon" -> "대지의 지배자";
            case "dialga" -> "시간의 주인";
            case "palkia" -> "공간의 주인";
            case "giratina" -> "반전세계의 목격자";
            case "reshiram" -> "진실의 용왕";
            case "zekrom" -> "이상의 용왕";
            case "kyurem" -> "경계의 용왕";
            case "koraidon" -> "고대의 용왕";
            case "miraidon" -> "미래의 용왕";
            case "zygarde" -> "균형의 감시자";
            case "xerneas" -> "생명의 수호자";
            case "yveltal" -> "파멸의 목격자";
            case "solgaleo" -> "태양의 사자";
            case "lunala" -> "달의 사자";
            case "necrozma" -> "빛을 삼킨 자";
            case "terapagos" -> "테라의 증인";
            case "zacian" -> "검의 수호자";
            case "zamazenta" -> "방패의 수호자";
            case "calyrex" -> "왕관의 계승자";
            case "eternatus" -> "무한의 목격자";
            case "mew" -> "태초의 목격자";
            case "mewtwo" -> "유전자의 초월자";
            case "arceus" -> "영원의 목격자";
            default -> "최초 최상위: " + fallbackDisplay;
        };
    }
}
