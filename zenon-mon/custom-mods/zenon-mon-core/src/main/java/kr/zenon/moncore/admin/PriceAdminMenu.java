package kr.zenon.moncore.admin;

import kr.zenon.moncore.config.ConfigManager;
import kr.zenon.moncore.config.EconomyConfig;
import kr.zenon.moncore.menu.MenuIcons;
import kr.zenon.moncore.menu.ServerMenuHandler;
import kr.zenon.moncore.shop.ShopLayout;
import kr.zenon.moncore.util.ChatInputManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 운영자 GUI — economy.json 상점 가격 편집. 클릭 → 채팅 숫자 입력 → 즉시 저장. */
public final class PriceAdminMenu {
    private PriceAdminMenu() {}

    private static final int PER_PAGE = ShopLayout.CONTENT_SLOTS.length;

    private record Category(String id, String label, Item icon) {}
    private record PriceEntry(String key, Text name, Item icon, long price, int minBadges) {}

    private static final List<Category> CATEGORIES = List.of(
            new Category("sell", "매입소", Items.EMERALD),
            new Category("buy", "편의 상점", Items.SNOWBALL),
            new Category("growth", "성장 상점", Items.EXPERIENCE_BOTTLE),
            new Category("training", "실전 육성", Items.ANVIL),
            new Category("tm", "기술머신", Items.PAPER),
            new Category("mega", "메가 연구소", Items.NETHER_STAR),
            new Category("altar", "전설 제단 해금", Items.END_CRYSTAL),
            new Category("ticket", "조우권 사용가", Items.ENDER_EYE),
            new Category("engineering", "포로공학", Items.SMITHING_TABLE)
    );

    public static void open(ServerPlayerEntity admin) {
        ServerMenuHandler.show(admin, Text.literal("가격 관리").formatted(Formatting.GOLD),
                PriceAdminMenu::populateCategories, PriceAdminMenu::onCategoryClick);
    }

    private static void populateCategories(Inventory inv) {
        for (int i = 0; i < ServerMenuHandler.DISPLAY_SIZE; i++) inv.setStack(i, MenuIcons.pane());
        inv.setStack(ShopLayout.BALANCE_SLOT, MenuIcons.icon(Items.GOLD_BLOCK, "§6가격 관리",
                List.of("§7카테고리를 선택하세요.", "§8변경값은 economy.json에 즉시 저장됩니다.")));
        inv.setStack(ShopLayout.BACK_SLOT, MenuIcons.icon(Items.ARROW, "§e← 운영자 패널", List.of()));
        for (int i = 0; i < CATEGORIES.size() && i < ShopLayout.CONTENT_SLOTS.length; i++) {
            Category c = CATEGORIES.get(i);
            inv.setStack(ShopLayout.CONTENT_SLOTS[i], MenuIcons.icon(c.icon, "§e" + c.label,
                    List.of("§7클릭 — 가격 목록 열기")));
        }
    }

    private static void onCategoryClick(ServerPlayerEntity admin, int slot, int button, boolean shift) {
        if (slot == ShopLayout.BACK_SLOT) { AdminMenu.open(admin); return; }
        int idx = ShopLayout.contentIndexOf(slot);
        if (idx < 0 || idx >= CATEGORIES.size()) return;
        openEntries(admin, CATEGORIES.get(idx), 0);
    }

    private static void openEntries(ServerPlayerEntity admin, Category category, int page) {
        ServerMenuHandler.show(admin, Text.literal(category.label).formatted(Formatting.GOLD),
                inv -> populateEntries(inv, category, page),
                (p, slot, button, shift) -> onEntryClick(p, category, page, slot));
    }

    private static void populateEntries(Inventory inv, Category category, int page) {
        for (int i = 0; i < ServerMenuHandler.DISPLAY_SIZE; i++) inv.setStack(i, MenuIcons.pane());
        List<PriceEntry> entries = entries(category.id);
        int pages = Math.max(1, (entries.size() + PER_PAGE - 1) / PER_PAGE);
        page = Math.max(0, Math.min(page, pages - 1));
        String unit = ConfigManager.economy().currencyDisplay;

        inv.setStack(ShopLayout.BALANCE_SLOT, MenuIcons.icon(category.icon, "§6" + category.label,
                List.of("§7클릭 → 채팅에 새 가격 입력", "§7페이지 §f" + (page + 1) + " / " + pages)));
        inv.setStack(ShopLayout.BACK_SLOT, MenuIcons.icon(Items.ARROW, "§e← 카테고리", List.of()));
        if (page > 0) inv.setStack(ShopLayout.PREV_SLOT, MenuIcons.icon(Items.SPECTRAL_ARROW, "§e◀ 이전 페이지", List.of()));
        if (page < pages - 1) inv.setStack(ShopLayout.NEXT_SLOT, MenuIcons.icon(Items.SPECTRAL_ARROW, "§e다음 페이지 ▶", List.of()));

        int start = page * PER_PAGE;
        for (int i = 0; i < PER_PAGE && start + i < entries.size(); i++) {
            PriceEntry e = entries.get(start + i);
            List<String> lore = new ArrayList<>();
            lore.add("§7현재 가격: §6" + e.price + " " + unit);
            if (e.minBadges >= 0) lore.add("§7배지 제한: §f" + e.minBadges + "개");
            lore.add("§8" + e.key);
            lore.add("§e클릭 — 가격 수정");
            inv.setStack(ShopLayout.CONTENT_SLOTS[i], MenuIcons.icon(e.icon,
                    MenuIcons.named(Formatting.WHITE, e.name), lore));
        }
    }

    private static void onEntryClick(ServerPlayerEntity admin, Category category, int page, int slot) {
        if (slot == ShopLayout.BACK_SLOT) { open(admin); return; }
        if (slot == ShopLayout.PREV_SLOT) { openEntries(admin, category, page - 1); return; }
        if (slot == ShopLayout.NEXT_SLOT) { openEntries(admin, category, page + 1); return; }

        int idx = ShopLayout.contentIndexOf(slot);
        if (idx < 0) return;
        List<PriceEntry> entries = entries(category.id);
        int globalIdx = page * PER_PAGE + idx;
        if (globalIdx >= entries.size()) return;
        PriceEntry entry = entries.get(globalIdx);

        admin.closeHandledScreen();
        admin.sendMessage(Text.literal("§6[가격관리] §f" + entry.key
                + "§7 현재 " + entry.price + " → 새 가격을 채팅에 입력하세요. §8(취소: 취소)"), false);
        ChatInputManager.await(admin, msg -> {
            if (msg.equals("취소") || msg.isBlank()) {
                admin.sendMessage(Text.literal("§7[가격관리] 취소했습니다."), false);
                openEntries(admin, category, page);
                return;
            }
            long value;
            try {
                value = Long.parseLong(msg);
            } catch (NumberFormatException e) {
                admin.sendMessage(Text.literal("§c[가격관리] 숫자만 입력할 수 있습니다."), false);
                openEntries(admin, category, page);
                return;
            }
            if (value < 0) {
                admin.sendMessage(Text.literal("§c[가격관리] 가격은 0 이상이어야 합니다."), false);
                openEntries(admin, category, page);
                return;
            }
            setPrice(category.id, entry.key, value);
            ConfigManager.saveEconomy();
            admin.sendMessage(Text.literal("§a[가격관리] " + entry.key + " = " + value + " 저장 완료."), false);
            openEntries(admin, category, page);
        });
    }

    private static List<PriceEntry> entries(String categoryId) {
        EconomyConfig cfg = ConfigManager.economy();
        return switch (categoryId) {
            case "sell" -> longEntries(cfg.sellPrices);
            case "buy" -> longEntries(cfg.buyPrices);
            case "growth" -> shopEntries(cfg.growthShop);
            case "training" -> shopEntries(cfg.trainingShop);
            case "tm" -> shopEntries(cfg.tmShop);
            case "mega" -> shopEntries(cfg.megaShop);
            case "altar" -> shopEntries(cfg.altarUnlock);
            case "ticket" -> longEntries(cfg.ticketUse);
            case "engineering" -> engineeringEntries(cfg.engineering);
            default -> List.of();
        };
    }

    private static List<PriceEntry> longEntries(Map<String, Long> map) {
        List<PriceEntry> list = new ArrayList<>();
        for (Map.Entry<String, Long> e : map.entrySet()) {
            Item item = resolve(e.getKey());
            list.add(new PriceEntry(e.getKey(), nameOf(item, e.getKey()), item == null ? Items.PAPER : item,
                    e.getValue(), -1));
        }
        return list;
    }

    private static List<PriceEntry> shopEntries(Map<String, EconomyConfig.ShopEntry> map) {
        List<PriceEntry> list = new ArrayList<>();
        for (Map.Entry<String, EconomyConfig.ShopEntry> e : map.entrySet()) {
            Item item = resolve(e.getKey());
            EconomyConfig.ShopEntry se = e.getValue();
            list.add(new PriceEntry(e.getKey(), nameOf(item, e.getKey()), item == null ? Items.PAPER : item,
                    se.price, se.minBadges));
        }
        return list;
    }

    private static List<PriceEntry> engineeringEntries(EconomyConfig.EngineeringConfig cfg) {
        List<PriceEntry> list = new ArrayList<>(List.of(
                new PriceEntry("stonePrice", Text.literal("기술 정수 구매가"), Items.AMETHYST_SHARD, cfg.stonePrice, cfg.stoneBadges),
                new PriceEntry("priceStatus", Text.literal("변화기 각인가"), Items.PAPER, cfg.priceStatus, -1),
                new PriceEntry("priceWeak", Text.literal("위력 60 이하 각인가"), Items.PAPER, cfg.priceWeak, -1),
                new PriceEntry("priceMedium", Text.literal("위력 61~90 각인가"), Items.PAPER, cfg.priceMedium, -1),
                new PriceEntry("priceStrong", Text.literal("위력 91~110 각인가"), Items.PAPER, cfg.priceStrong, -1),
                new PriceEntry("pricePremium", Text.literal("위력 111+ 각인가"), Items.PAPER, cfg.pricePremium, -1)));
        if (cfg.abilityMakeoverEnabled) {
            list.add(new PriceEntry("abilityStonePrice", Text.literal("특성 정수 구매가"), Items.NETHER_STAR, cfg.abilityStonePrice, cfg.abilityStoneBadges));
            list.add(new PriceEntry("abilityChangePrice", Text.literal("특성 변경 각인가"), Items.NETHER_STAR, cfg.abilityChangePrice, -1));
        }
        return list;
    }

    private static void setPrice(String categoryId, String key, long value) {
        EconomyConfig cfg = ConfigManager.economy();
        switch (categoryId) {
            case "sell" -> cfg.sellPrices.put(key, value);
            case "buy" -> cfg.buyPrices.put(key, value);
            case "growth" -> cfg.growthShop.get(key).price = value;
            case "training" -> cfg.trainingShop.get(key).price = value;
            case "tm" -> cfg.tmShop.get(key).price = value;
            case "mega" -> cfg.megaShop.get(key).price = value;
            case "altar" -> cfg.altarUnlock.get(key).price = value;
            case "ticket" -> cfg.ticketUse.put(key, value);
            case "engineering" -> setEngineeringPrice(cfg.engineering, key, value);
            default -> { /* ignore */ }
        }
    }

    private static void setEngineeringPrice(EconomyConfig.EngineeringConfig cfg, String key, long value) {
        switch (key) {
            case "stonePrice" -> cfg.stonePrice = value;
            case "priceStatus" -> cfg.priceStatus = value;
            case "priceWeak" -> cfg.priceWeak = value;
            case "priceMedium" -> cfg.priceMedium = value;
            case "priceStrong" -> cfg.priceStrong = value;
            case "pricePremium" -> cfg.pricePremium = value;
            case "abilityStonePrice" -> cfg.abilityStonePrice = value;
            case "abilityChangePrice" -> cfg.abilityChangePrice = value;
            default -> { /* ignore */ }
        }
    }

    private static Text nameOf(Item item, String key) {
        return item == null ? Text.literal(key) : item.getName();
    }

    private static Item resolve(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null || !Registries.ITEM.containsId(id)) return null;
        return Registries.ITEM.get(id);
    }
}
