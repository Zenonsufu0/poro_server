package kr.zenon.moncore.admin;

import kr.zenon.moncore.data.PlayerProgress;
import kr.zenon.moncore.data.ZenonMonState;
import kr.zenon.moncore.menu.MenuIcons;
import kr.zenon.moncore.menu.ServerMenuHandler;
import kr.zenon.moncore.shop.ShopLayout;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 운영자 GUI — 경제 모니터(텔레메트리). 총량/유저/품목/소모처/최근 거래를 확인한다.
 */
public final class EconomyMonitorMenu {
    private EconomyMonitorMenu() {}

    private static final int MODE_OVERVIEW = 0;
    private static final int MODE_PLAYERS = 1;
    private static final int MODE_SELL_ITEMS = 2;
    private static final int MODE_SINKS = 3;
    private static final int MODE_RECENT = 4;

    private static final int SLOT_OVERVIEW = 0;
    private static final int SLOT_PLAYERS = 1;
    private static final int SLOT_SELL_ITEMS = 2;
    private static final int SLOT_SINKS = 6;
    private static final int SLOT_RECENT = 7;
    private static final int[] DATA_SLOTS = {
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public static void open(ServerPlayerEntity admin) {
        open(admin, MODE_OVERVIEW);
    }

    private static void open(ServerPlayerEntity admin, int mode) {
        ServerMenuHandler.show(admin, Text.literal("경제 모니터").formatted(Formatting.GOLD),
                inv -> populate(inv, admin, mode), (p, slot, button, shift) -> onClick(p, slot, mode));
    }

    private static void populate(Inventory inv, ServerPlayerEntity admin, int mode) {
        for (int i = 0; i < ServerMenuHandler.DISPLAY_SIZE; i++) inv.setStack(i, MenuIcons.pane());
        ZenonMonState s = ZenonMonState.get(admin.getServer());

        long totalIn = s.goldIn.values().stream().mapToLong(Long::longValue).sum();
        long totalOut = s.goldOut.values().stream().mapToLong(Long::longValue).sum();
        long totalBalance = s.all().values().stream().mapToLong(p -> p.balance).sum();
        inv.setStack(ShopLayout.BALANCE_SLOT, MenuIcons.icon(Items.GOLD_BLOCK, "§6골드 흐름 총계",
                List.of("§e현재 보유 총량: §f" + totalBalance,
                        "§a누적 생산량: §f" + totalIn, "§c누적 소모량: §f" + totalOut,
                        "§7순증: §f" + (totalIn - totalOut))));
        inv.setStack(ShopLayout.BACK_SLOT, MenuIcons.icon(Items.ARROW, "§e← 운영자 패널", List.of()));

        addTab(inv, SLOT_OVERVIEW, mode == MODE_OVERVIEW, Items.COMPASS, "총계");
        addTab(inv, SLOT_PLAYERS, mode == MODE_PLAYERS, Items.PLAYER_HEAD, "유저");
        addTab(inv, SLOT_SELL_ITEMS, mode == MODE_SELL_ITEMS, Items.EMERALD, "판매 품목");
        addTab(inv, SLOT_SINKS, mode == MODE_SINKS, Items.HOPPER, "소모처");
        addTab(inv, SLOT_RECENT, mode == MODE_RECENT, Items.CLOCK, "최근 거래");

        switch (mode) {
            case MODE_PLAYERS -> populatePlayers(inv, admin.getServer(), s);
            case MODE_SELL_ITEMS -> populateSellItems(inv, s);
            case MODE_SINKS -> populateSinks(inv, s);
            case MODE_RECENT -> populateRecent(inv, s);
            default -> populateOverview(inv, s);
        }
    }

    private static void addTab(Inventory inv, int slot, boolean selected, Item item, String label) {
        inv.setStack(slot, MenuIcons.icon(item, (selected ? "§a" : "§7") + label,
                List.of(selected ? "§a선택됨" : "§e클릭")));
    }

    private static void populateOverview(Inventory inv, ZenonMonState s) {
        List<String> groups = new ArrayList<>();
        groups.addAll(s.goldIn.keySet());
        for (String key : s.goldOut.keySet()) if (!groups.contains(key)) groups.add(key);
        groups.sort(String::compareTo);
        int idx = 0;
        for (String g : groups) {
            if (idx >= DATA_SLOTS.length) break;
            long in = s.goldIn.getOrDefault(g, 0L);
            long out = s.goldOut.getOrDefault(g, 0L);
            inv.setStack(DATA_SLOTS[idx++], MenuIcons.icon(Items.PAPER,
                    "§f" + g, List.of("§a생산 +" + in, "§c소모 -" + out, "§7순증 " + (in - out))));
        }
        if (idx == 0) empty(inv, "아직 골드 거래가 없습니다");
    }

    private static void populatePlayers(Inventory inv, MinecraftServer server, ZenonMonState s) {
        List<Map.Entry<UUID, PlayerProgress>> list = new ArrayList<>(s.all().entrySet());
        list.sort(Comparator.<Map.Entry<UUID, PlayerProgress>>comparingLong(e -> e.getValue().balance).reversed());
        int idx = 0;
        for (Map.Entry<UUID, PlayerProgress> e : list) {
            if (idx >= DATA_SLOTS.length) break;
            UUID uuid = e.getKey();
            PlayerProgress p = e.getValue();
            long in = s.playerGoldIn.getOrDefault(uuid, 0L);
            long out = s.playerGoldOut.getOrDefault(uuid, 0L);
            inv.setStack(DATA_SLOTS[idx++], MenuIcons.icon(Items.PLAYER_HEAD,
                    "§f" + playerName(server, uuid, p), List.of(
                            "§6현재 잔액: §f" + p.balance,
                            "§a누적 생산: §f" + in,
                            "§c누적 소모: §f" + out,
                            "§7UUID: " + shortUuid(uuid))));
        }
        if (idx == 0) empty(inv, "기록된 플레이어가 없습니다");
    }

    private static void populateSellItems(Inventory inv, ZenonMonState s) {
        List<Map.Entry<String, Long>> list = sortedByValueDesc(s.itemSellGold);
        int idx = 0;
        for (Map.Entry<String, Long> e : list) {
            if (idx >= DATA_SLOTS.length) break;
            String itemId = e.getKey();
            long gold = e.getValue();
            long count = s.itemSellCount.getOrDefault(itemId, 0L);
            inv.setStack(DATA_SLOTS[idx++], MenuIcons.icon(resolveItem(itemId),
                    "§f" + itemId, List.of("§a생산 골드: §f" + gold, "§7판매 수량: §f" + count)));
        }
        if (idx == 0) empty(inv, "판매 품목 기록이 없습니다");
    }

    private static void populateSinks(Inventory inv, ZenonMonState s) {
        List<Map.Entry<String, Long>> list = sortedByValueDesc(s.goldOutBySource);
        int idx = 0;
        for (Map.Entry<String, Long> e : list) {
            if (idx >= DATA_SLOTS.length) break;
            inv.setStack(DATA_SLOTS[idx++], MenuIcons.icon(Items.HOPPER,
                    "§f" + e.getKey(), List.of("§c소모 골드: §f" + e.getValue())));
        }
        if (idx == 0) empty(inv, "소모 기록이 없습니다");
    }

    private static void populateRecent(Inventory inv, ZenonMonState s) {
        int idx = 0;
        List<ZenonMonState.EconomyTransaction> txs = s.economyTransactions;
        for (int i = txs.size() - 1; i >= 0 && idx < DATA_SLOTS.length; i--) {
            ZenonMonState.EconomyTransaction tx = txs.get(i);
            boolean plus = tx.delta() >= 0;
            List<String> lore = new ArrayList<>();
            lore.add((plus ? "§a변동: +" : "§c변동: ") + tx.delta());
            lore.add("§6잔액: §f" + tx.balanceAfter());
            lore.add("§7source: " + tx.source());
            if (!tx.itemId().isBlank()) lore.add("§7item: " + tx.itemId() + " ×" + tx.count());
            lore.add("§8" + tx.playerUuid());
            inv.setStack(DATA_SLOTS[idx++], MenuIcons.icon(plus ? Items.GOLD_NUGGET : Items.REDSTONE,
                    "§f" + tx.playerName(), lore));
        }
        if (idx == 0) empty(inv, "최근 거래가 없습니다");
    }

    private static List<Map.Entry<String, Long>> sortedByValueDesc(Map<String, Long> map) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.<String, Long>comparingByValue().reversed());
        return list;
    }

    private static Item resolveItem(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null || !Registries.ITEM.containsId(id)) return Items.PAPER;
        return Registries.ITEM.get(id);
    }

    private static String playerName(MinecraftServer server, UUID uuid, PlayerProgress p) {
        ServerPlayerEntity online = server.getPlayerManager().getPlayer(uuid);
        if (online != null) return online.getGameProfile().getName();
        if (p.lastKnownName != null && !p.lastKnownName.isBlank()) return p.lastKnownName;
        return shortUuid(uuid);
    }

    private static String shortUuid(UUID uuid) {
        String s = uuid.toString();
        return s.length() <= 8 ? s : s.substring(0, 8);
    }

    private static void empty(Inventory inv, String message) {
        inv.setStack(DATA_SLOTS[0], MenuIcons.icon(Items.BARRIER, "§7기록 없음", List.of("§8" + message)));
    }

    private static void onClick(ServerPlayerEntity admin, int slot, int mode) {
        if (slot == ShopLayout.BACK_SLOT) { AdminMenu.open(admin); return; }
        if (slot == SLOT_OVERVIEW) { open(admin, MODE_OVERVIEW); return; }
        if (slot == SLOT_PLAYERS) { open(admin, MODE_PLAYERS); return; }
        if (slot == SLOT_SELL_ITEMS) { open(admin, MODE_SELL_ITEMS); return; }
        if (slot == SLOT_SINKS) { open(admin, MODE_SINKS); return; }
        if (slot == SLOT_RECENT) open(admin, MODE_RECENT);
    }
}
