package kr.zenon.moncore.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 월드 부속 PersistentState (database_schema.md §1: 0.1 = PersistentState 채택).
 * 오버월드 DimensionDataStorage 의 "zenonmoncore_progress" 키. 월드 백업/롤백과 자동 정합.
 */
public class ZenonMonState extends PersistentState {
    private static final String KEY = "zenonmoncore_progress";

    private final Map<UUID, PlayerProgress> players = new HashMap<>();

    // 전역 이벤트 부스트 (운영자 GUI 토글, 영속) — 결정/IB-002
    public boolean xpBoost = false;
    public boolean goldBoost = false;
    public boolean apexBoost = false;   // 컨셉 조우 최상급 가중 ×2 (결정 035, 이벤트)
    public boolean fieldEventFast = false; // 전설 필드 이벤트 주기 2배 단축 (결정 038, 이벤트)

    // 챔피언스리그 역대 챔피언 (결정 040, 챔피언 홀): "이름" 순서대로
    public final java.util.List<String> championHistory = new java.util.ArrayList<>();

    // 경제 텔레메트리 (economy_design §6): 출처 그룹별 골드 유입/유출 누적
    public final Map<String, Long> goldIn = new java.util.LinkedHashMap<>();
    public final Map<String, Long> goldOut = new java.util.LinkedHashMap<>();
    public final Map<String, Long> goldInBySource = new java.util.LinkedHashMap<>();
    public final Map<String, Long> goldOutBySource = new java.util.LinkedHashMap<>();
    public final Map<UUID, Long> playerGoldIn = new java.util.LinkedHashMap<>();
    public final Map<UUID, Long> playerGoldOut = new java.util.LinkedHashMap<>();
    public final Map<String, Long> itemSellCount = new java.util.LinkedHashMap<>();
    public final Map<String, Long> itemSellGold = new java.util.LinkedHashMap<>();
    public final Map<String, Long> itemBuyCount = new java.util.LinkedHashMap<>();
    public final Map<String, Long> itemBuyGold = new java.util.LinkedHashMap<>();
    public final List<EconomyTransaction> economyTransactions = new ArrayList<>();
    public static final int ECONOMY_TRANSACTION_LIMIT = 500;

    // 칭호 전역 기록: 최고 보유 골드 갱신자 + 최상위 전설 최초 포획자(종별 1명)
    public UUID wealthRecordUuid = null;
    public long wealthRecordBalance = 0L;
    public String wealthRecordName = "";
    public final Map<String, UUID> firstApexCatchPlayer = new java.util.LinkedHashMap<>();
    public final Map<String, String> firstApexCatchName = new java.util.LinkedHashMap<>();
    public final Map<String, String> firstApexCatchDisplayName = new java.util.LinkedHashMap<>();
    public final Map<String, Long> firstApexCatchEpochMillis = new java.util.LinkedHashMap<>();

    // 디스코드 운영 API 연동용 마크 제재 이력(디스코드 제재와 별개)
    public long nextSanctionId = 1L;
    public final List<SanctionRecord> sanctions = new ArrayList<>();

    public static final PersistentState.Type<ZenonMonState> TYPE = new PersistentState.Type<>(
            ZenonMonState::new,
            ZenonMonState::readNbt,
            null
    );

    /** 서버 오버월드의 영속 상태를 가져온다(없으면 생성). */
    public static ZenonMonState get(MinecraftServer server) {
        ServerWorld overworld = server.getOverworld();
        PersistentStateManager psm = overworld.getPersistentStateManager();
        return psm.getOrCreate(TYPE, KEY);
    }

    /** 진행 엔트리 조회(없으면 생성 + dirty). */
    public PlayerProgress getOrCreate(UUID uuid) {
        PlayerProgress existing = players.get(uuid);
        if (existing != null) return existing;
        PlayerProgress fresh = new PlayerProgress();
        players.put(uuid, fresh);
        markDirty();
        return fresh;
    }

    /** 조회만(없으면 null). */
    public PlayerProgress peek(UUID uuid) {
        return players.get(uuid);
    }

    /** 전체 플레이어 진행(읽기용 — 순위표 등). */
    public java.util.Map<UUID, PlayerProgress> all() {
        return java.util.Collections.unmodifiableMap(players);
    }

    public void recordEconomyFlow(UUID uuid, String playerName, long delta, long balanceAfter,
                                  String source, String itemId, int count) {
        String src = safe(source);
        String group = group(src);
        if (delta > 0) {
            goldIn.merge(group, delta, Long::sum);
            goldInBySource.merge(src, delta, Long::sum);
            playerGoldIn.merge(uuid, delta, Long::sum);
            if (src.startsWith("sell:") && itemId != null && count > 0) {
                itemSellCount.merge(itemId, (long) count, Long::sum);
                itemSellGold.merge(itemId, delta, Long::sum);
            }
        } else if (delta < 0) {
            long amount = -delta;
            goldOut.merge(group, amount, Long::sum);
            goldOutBySource.merge(src, amount, Long::sum);
            playerGoldOut.merge(uuid, amount, Long::sum);
            if (!src.contains("refund") && itemId != null && count > 0) {
                itemBuyCount.merge(itemId, (long) count, Long::sum);
                itemBuyGold.merge(itemId, amount, Long::sum);
            }
        }
        addEconomyTransaction(new EconomyTransaction(System.currentTimeMillis(), uuid, safe(playerName),
                delta, balanceAfter, src, itemId == null ? "" : itemId, Math.max(0, count)));
        markDirty();
    }

    public void recordEconomyAdjustment(UUID uuid, String playerName, long delta, long balanceAfter, String source) {
        addEconomyTransaction(new EconomyTransaction(System.currentTimeMillis(), uuid, safe(playerName),
                delta, balanceAfter, safe(source), "", 0));
        markDirty();
    }

    private void addEconomyTransaction(EconomyTransaction tx) {
        economyTransactions.add(tx);
        while (economyTransactions.size() > ECONOMY_TRANSACTION_LIMIT) economyTransactions.remove(0);
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "기타" : value;
    }

    /** 출처 태그를 그룹키로(콜론 앞). 예 sell:minecraft:diamond -> sell. */
    private static String group(String source) {
        int i = source.indexOf(':');
        return i > 0 ? source.substring(0, i) : source;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound playersNbt = new NbtCompound();
        for (Map.Entry<UUID, PlayerProgress> e : players.entrySet()) {
            playersNbt.put(e.getKey().toString(), e.getValue().writeNbt(new NbtCompound()));
        }
        nbt.put("players", playersNbt);
        nbt.putBoolean("xpBoost", xpBoost);
        nbt.putBoolean("goldBoost", goldBoost);
        nbt.putBoolean("apexBoost", apexBoost);
        nbt.putBoolean("fieldEventFast", fieldEventFast);
        nbt.put("goldIn", longMapToNbt(goldIn));
        nbt.put("goldOut", longMapToNbt(goldOut));
        nbt.put("goldInBySource", longMapToNbt(goldInBySource));
        nbt.put("goldOutBySource", longMapToNbt(goldOutBySource));
        nbt.put("playerGoldIn", uuidLongMapToNbt(playerGoldIn));
        nbt.put("playerGoldOut", uuidLongMapToNbt(playerGoldOut));
        nbt.put("itemSellCount", longMapToNbt(itemSellCount));
        nbt.put("itemSellGold", longMapToNbt(itemSellGold));
        nbt.put("itemBuyCount", longMapToNbt(itemBuyCount));
        nbt.put("itemBuyGold", longMapToNbt(itemBuyGold));
        NbtList txs = new NbtList();
        for (EconomyTransaction tx : economyTransactions) txs.add(tx.writeNbt());
        nbt.put("economyTransactions", txs);
        NbtCompound wealthRecord = new NbtCompound();
        if (wealthRecordUuid != null) wealthRecord.putString("uuid", wealthRecordUuid.toString());
        wealthRecord.putLong("balance", wealthRecordBalance);
        wealthRecord.putString("name", wealthRecordName == null ? "" : wealthRecordName);
        nbt.put("wealthRecord", wealthRecord);
        nbt.put("firstApexCatch", firstApexCatchToNbt());
        nbt.putLong("nextSanctionId", nextSanctionId);
        NbtList sanctionsNbt = new NbtList();
        for (SanctionRecord sanction : sanctions) sanctionsNbt.add(sanction.writeNbt());
        nbt.put("sanctions", sanctionsNbt);
        NbtList champs = new NbtList();
        for (String c : championHistory) champs.add(net.minecraft.nbt.NbtString.of(c));
        nbt.put("championHistory", champs);
        return nbt;
    }

    private static NbtCompound longMapToNbt(Map<String, Long> map) {
        NbtCompound c = new NbtCompound();
        for (Map.Entry<String, Long> e : map.entrySet()) c.putLong(e.getKey(), e.getValue());
        return c;
    }

    private static void nbtToLongMap(NbtCompound c, Map<String, Long> map) {
        for (String k : c.getKeys()) map.put(k, c.getLong(k));
    }

    private static NbtCompound uuidLongMapToNbt(Map<UUID, Long> map) {
        NbtCompound c = new NbtCompound();
        for (Map.Entry<UUID, Long> e : map.entrySet()) c.putLong(e.getKey().toString(), e.getValue());
        return c;
    }

    private static void nbtToUuidLongMap(NbtCompound c, Map<UUID, Long> map) {
        for (String k : c.getKeys()) {
            try {
                map.put(UUID.fromString(k), c.getLong(k));
            } catch (IllegalArgumentException ignored) {
                // 잘못된 UUID 키는 건너뜀
            }
        }
    }

    private NbtCompound firstApexCatchToNbt() {
        NbtCompound out = new NbtCompound();
        for (Map.Entry<String, UUID> e : firstApexCatchPlayer.entrySet()) {
            String species = e.getKey();
            NbtCompound c = new NbtCompound();
            c.putString("playerUuid", e.getValue().toString());
            c.putString("playerName", firstApexCatchName.getOrDefault(species, ""));
            c.putString("displayName", firstApexCatchDisplayName.getOrDefault(species, species));
            c.putLong("epochMillis", firstApexCatchEpochMillis.getOrDefault(species, 0L));
            out.put(species, c);
        }
        return out;
    }

    private static void nbtToFirstApexCatch(NbtCompound c, ZenonMonState state) {
        for (String species : c.getKeys()) {
            if (!c.contains(species, NbtElement.COMPOUND_TYPE)) continue;
            NbtCompound row = c.getCompound(species);
            try {
                state.firstApexCatchPlayer.put(species, UUID.fromString(row.getString("playerUuid")));
                state.firstApexCatchName.put(species, row.getString("playerName"));
                state.firstApexCatchDisplayName.put(species, row.getString("displayName"));
                state.firstApexCatchEpochMillis.put(species, row.getLong("epochMillis"));
            } catch (IllegalArgumentException ignored) {
                // 잘못된 UUID 키는 건너뜀
            }
        }
    }

    public static ZenonMonState readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        ZenonMonState state = new ZenonMonState();
        if (nbt.contains("players", NbtElement.COMPOUND_TYPE)) {
            NbtCompound playersNbt = nbt.getCompound("players");
            for (String key : playersNbt.getKeys()) {
                try {
                    state.players.put(UUID.fromString(key), PlayerProgress.readNbt(playersNbt.getCompound(key)));
                } catch (IllegalArgumentException ignored) {
                    // 잘못된 UUID 키는 건너뜀
                }
            }
        }
        state.xpBoost = nbt.getBoolean("xpBoost");
        state.goldBoost = nbt.getBoolean("goldBoost");
        state.apexBoost = nbt.getBoolean("apexBoost");
        state.fieldEventFast = nbt.getBoolean("fieldEventFast");
        if (nbt.contains("goldIn", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("goldIn"), state.goldIn);
        if (nbt.contains("goldOut", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("goldOut"), state.goldOut);
        if (nbt.contains("goldInBySource", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("goldInBySource"), state.goldInBySource);
        if (nbt.contains("goldOutBySource", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("goldOutBySource"), state.goldOutBySource);
        if (nbt.contains("playerGoldIn", NbtElement.COMPOUND_TYPE)) nbtToUuidLongMap(nbt.getCompound("playerGoldIn"), state.playerGoldIn);
        if (nbt.contains("playerGoldOut", NbtElement.COMPOUND_TYPE)) nbtToUuidLongMap(nbt.getCompound("playerGoldOut"), state.playerGoldOut);
        if (nbt.contains("itemSellCount", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("itemSellCount"), state.itemSellCount);
        if (nbt.contains("itemSellGold", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("itemSellGold"), state.itemSellGold);
        if (nbt.contains("itemBuyCount", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("itemBuyCount"), state.itemBuyCount);
        if (nbt.contains("itemBuyGold", NbtElement.COMPOUND_TYPE)) nbtToLongMap(nbt.getCompound("itemBuyGold"), state.itemBuyGold);
        if (nbt.contains("economyTransactions", NbtElement.LIST_TYPE)) {
            NbtList txs = nbt.getList("economyTransactions", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < txs.size(); i++) state.economyTransactions.add(EconomyTransaction.readNbt(txs.getCompound(i)));
        }
        if (nbt.contains("wealthRecord", NbtElement.COMPOUND_TYPE)) {
            NbtCompound wr = nbt.getCompound("wealthRecord");
            try {
                String raw = wr.getString("uuid");
                state.wealthRecordUuid = raw.isBlank() ? null : UUID.fromString(raw);
            } catch (IllegalArgumentException ignored) {
                state.wealthRecordUuid = null;
            }
            state.wealthRecordBalance = wr.getLong("balance");
            state.wealthRecordName = wr.getString("name");
        }
        if (nbt.contains("firstApexCatch", NbtElement.COMPOUND_TYPE)) {
            nbtToFirstApexCatch(nbt.getCompound("firstApexCatch"), state);
        }
        if (nbt.contains("nextSanctionId", NbtElement.LONG_TYPE)) {
            state.nextSanctionId = Math.max(1L, nbt.getLong("nextSanctionId"));
        }
        if (nbt.contains("sanctions", NbtElement.LIST_TYPE)) {
            NbtList sanctionsNbt = nbt.getList("sanctions", NbtElement.COMPOUND_TYPE);
            long maxId = 0L;
            for (int i = 0; i < sanctionsNbt.size(); i++) {
                SanctionRecord sanction = SanctionRecord.readNbt(sanctionsNbt.getCompound(i));
                state.sanctions.add(sanction);
                maxId = Math.max(maxId, sanction.id());
            }
            state.nextSanctionId = Math.max(state.nextSanctionId, maxId + 1L);
        }
        if (nbt.contains("championHistory", NbtElement.LIST_TYPE)) {
            NbtList champs = nbt.getList("championHistory", NbtElement.STRING_TYPE);
            for (int i = 0; i < champs.size(); i++) state.championHistory.add(champs.getString(i));
        }
        return state;
    }

    public record EconomyTransaction(long epochMillis, UUID playerUuid, String playerName, long delta,
                                     long balanceAfter, String source, String itemId, int count) {
        private NbtCompound writeNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putLong("epochMillis", epochMillis);
            nbt.putString("playerUuid", playerUuid.toString());
            nbt.putString("playerName", playerName);
            nbt.putLong("delta", delta);
            nbt.putLong("balanceAfter", balanceAfter);
            nbt.putString("source", source);
            nbt.putString("itemId", itemId);
            nbt.putInt("count", count);
            return nbt;
        }

        private static EconomyTransaction readNbt(NbtCompound nbt) {
            UUID uuid;
            try {
                uuid = UUID.fromString(nbt.getString("playerUuid"));
            } catch (IllegalArgumentException ex) {
                uuid = new UUID(0L, 0L);
            }
            return new EconomyTransaction(
                    nbt.getLong("epochMillis"),
                    uuid,
                    nbt.getString("playerName"),
                    nbt.getLong("delta"),
                    nbt.getLong("balanceAfter"),
                    nbt.getString("source"),
                    nbt.getString("itemId"),
                    nbt.getInt("count"));
        }
    }

    public record SanctionRecord(long id, long epochMillis, String action, UUID playerUuid, String playerName,
                                 String target, String operatorDiscordId, String reason, String source) {
        private NbtCompound writeNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putLong("id", id);
            nbt.putLong("epochMillis", epochMillis);
            nbt.putString("action", action);
            nbt.putString("playerUuid", playerUuid == null ? "" : playerUuid.toString());
            nbt.putString("playerName", playerName == null ? "" : playerName);
            nbt.putString("target", target == null ? "" : target);
            nbt.putString("operatorDiscordId", operatorDiscordId == null ? "" : operatorDiscordId);
            nbt.putString("reason", reason == null ? "" : reason);
            nbt.putString("source", source == null ? "" : source);
            return nbt;
        }

        private static SanctionRecord readNbt(NbtCompound nbt) {
            UUID uuid = null;
            try {
                String raw = nbt.getString("playerUuid");
                if (!raw.isBlank()) uuid = UUID.fromString(raw);
            } catch (IllegalArgumentException ignored) {
                uuid = null;
            }
            return new SanctionRecord(
                    nbt.getLong("id"),
                    nbt.getLong("epochMillis"),
                    nbt.getString("action"),
                    uuid,
                    nbt.getString("playerName"),
                    nbt.getString("target"),
                    nbt.getString("operatorDiscordId"),
                    nbt.getString("reason"),
                    nbt.getString("source"));
        }
    }
}
