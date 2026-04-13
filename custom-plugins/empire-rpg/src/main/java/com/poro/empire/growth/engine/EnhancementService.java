package com.poro.empire.growth.engine;

import com.poro.empire.common.registry.master.ItemMasterRegistry;
import com.poro.empire.common.registry.master.model.ItemMaster;
import com.poro.empire.common.result.ErrorCode;
import com.poro.empire.common.result.Result;

import java.util.Objects;

public final class EnhancementService {
    public static final int MAX_ENHANCE_LEVEL = 25;
    public static final String CURRENCY_GOLD = "gold";
    public static final String MATERIAL_ENHANCE_STONE = "enhancement_stone";

    private final ItemMasterRegistry itemMasterRegistry;
    private final EnhancementRuleRegistry enhancementRuleRegistry;
    private final EnhancementLogHook enhancementLogHook;
    private final RandomProvider randomProvider;

    public EnhancementService(
            ItemMasterRegistry itemMasterRegistry,
            EnhancementRuleRegistry enhancementRuleRegistry,
            EnhancementLogHook enhancementLogHook,
            RandomProvider randomProvider
    ) {
        this.itemMasterRegistry = Objects.requireNonNull(itemMasterRegistry, "itemMasterRegistry");
        this.enhancementRuleRegistry = Objects.requireNonNull(enhancementRuleRegistry, "enhancementRuleRegistry");
        this.enhancementLogHook = Objects.requireNonNull(enhancementLogHook, "enhancementLogHook");
        this.randomProvider = Objects.requireNonNull(randomProvider, "randomProvider");
    }

    public Result<EnhancementResult> attempt(PlayerGrowthState state, String itemInstanceId) {
        return attempt(state, itemInstanceId, null);
    }

    public Result<EnhancementResult> attempt(PlayerGrowthState state, String itemInstanceId, Double fixedRoll) {
        Objects.requireNonNull(state, "state");
        PlayerEquipmentItem item = state.inventoryItem(itemInstanceId).orElse(null);
        if (item == null) {
            return Result.failure(ErrorCode.INVALID_ARGUMENT, "Item instance not found: " + itemInstanceId);
        }

        ItemMaster itemMaster = itemMasterRegistry.find(item.itemId()).orElse(null);
        if (itemMaster == null) {
            return Result.failure(ErrorCode.INVALID_ARGUMENT, "Unknown item master for enhancement: " + item.itemId());
        }

        int currentLevel = item.enhanceLevel();
        if (currentLevel >= MAX_ENHANCE_LEVEL) {
            return Result.failure(ErrorCode.INVALID_ARGUMENT, "Enhancement is already at max level: " + currentLevel);
        }

        int targetLevel = currentLevel + 1;
        GrowthTier tier = GrowthTier.from(itemMaster.tier());
        EnhancementRule rule = enhancementRuleRegistry.find(tier, targetLevel).orElse(null);
        if (rule == null) {
            return Result.failure(
                    ErrorCode.INVALID_ARGUMENT,
                    "Enhancement rule not found. tier=" + tier + ", level=" + targetLevel
            );
        }

        if (!state.consumeCurrency(CURRENCY_GOLD, rule.goldCost())) {
            return Result.failure(
                    ErrorCode.INVALID_ARGUMENT,
                    "Not enough gold. required=" + rule.goldCost() + ", current=" + state.currency(CURRENCY_GOLD)
            );
        }
        if (!state.consumeCurrency(MATERIAL_ENHANCE_STONE, rule.stoneCost())) {
            state.addCurrency(CURRENCY_GOLD, rule.goldCost());
            return Result.failure(
                    ErrorCode.INVALID_ARGUMENT,
                    "Not enough enhancement stone. required=" + rule.stoneCost() + ", current=" + state.currency(MATERIAL_ENHANCE_STONE)
            );
        }

        double roll = fixedRoll == null ? randomProvider.nextDouble() : fixedRoll;
        boolean success = roll <= (rule.successRate() / 100.0d);
        int finalLevel = currentLevel;
        if (success) {
            finalLevel = targetLevel;
            item.setEnhanceLevel(finalLevel);
        }

        EnhancementResult result = new EnhancementResult(
                state.userId(),
                item.itemInstanceId(),
                item.itemId(),
                tier.name(),
                currentLevel,
                targetLevel,
                finalLevel,
                success,
                rule.successRate(),
                roll,
                rule.goldCost(),
                rule.stoneCost()
        );
        enhancementLogHook.onAttempt(result);
        return Result.success(result);
    }

    public record EnhancementResult(
            String userId,
            String itemInstanceId,
            String itemId,
            String tier,
            int beforeLevel,
            int targetLevel,
            int finalLevel,
            boolean success,
            double successRate,
            double roll,
            long goldCost,
            long stoneCost
    ) {
    }
}
