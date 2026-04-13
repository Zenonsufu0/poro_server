package com.poro.empire.growth.engine;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class PlayerGrowthState {
    private final String userId;
    private final String classId;
    private final Map<String, PlayerEquipmentItem> inventory = new LinkedHashMap<>();
    private final Map<EquipmentSlot, String> equippedItemBySlot = new LinkedHashMap<>();
    private final Map<String, Long> wallet = new LinkedHashMap<>();
    private final Map<Integer, String> equippedRunes = new LinkedHashMap<>();
    private final Map<Integer, EquippedCommonEngraving> commonEngravings = new LinkedHashMap<>();

    private String classEngravingId = "";

    public PlayerGrowthState(String userId, String classId) {
        this.userId = normalize(userId);
        this.classId = normalize(classId);
    }

    public String userId() {
        return userId;
    }

    public String classId() {
        return classId;
    }

    public void addInventoryItem(PlayerEquipmentItem item) {
        inventory.put(item.itemInstanceId(), item);
    }

    public Optional<PlayerEquipmentItem> inventoryItem(String itemInstanceId) {
        return Optional.ofNullable(inventory.get(normalize(itemInstanceId)));
    }

    public Map<String, PlayerEquipmentItem> inventorySnapshot() {
        return Map.copyOf(new LinkedHashMap<>(inventory));
    }

    public void equipItem(EquipmentSlot slot, String itemInstanceId) {
        equippedItemBySlot.put(slot, normalize(itemInstanceId));
    }

    public Optional<String> equippedItemInstanceId(EquipmentSlot slot) {
        return Optional.ofNullable(equippedItemBySlot.get(slot));
    }

    public Optional<PlayerEquipmentItem> equippedItem(EquipmentSlot slot) {
        return equippedItemInstanceId(slot).flatMap(this::inventoryItem);
    }

    public void unequipItem(EquipmentSlot slot) {
        equippedItemBySlot.remove(slot);
    }

    public Map<EquipmentSlot, String> equippedItems() {
        return Map.copyOf(new LinkedHashMap<>(equippedItemBySlot));
    }

    public long currency(String code) {
        return wallet.getOrDefault(normalize(code), 0L);
    }

    public void addCurrency(String code, long amount) {
        if (amount <= 0) {
            return;
        }
        wallet.merge(normalize(code), amount, Long::sum);
    }

    public boolean consumeCurrency(String code, long amount) {
        if (amount <= 0) {
            return true;
        }
        String normalized = normalize(code);
        long current = wallet.getOrDefault(normalized, 0L);
        if (current < amount) {
            return false;
        }
        wallet.put(normalized, current - amount);
        return true;
    }

    public Map<String, Long> walletSnapshot() {
        return Map.copyOf(new LinkedHashMap<>(wallet));
    }

    public void equipRune(int slotNo, String runeId) {
        equippedRunes.put(slotNo, normalize(runeId));
    }

    public void unequipRune(int slotNo) {
        equippedRunes.remove(slotNo);
    }

    public Map<Integer, String> equippedRunes() {
        return Map.copyOf(new LinkedHashMap<>(equippedRunes));
    }

    public String classEngravingId() {
        return classEngravingId;
    }

    public void setClassEngravingId(String classEngravingId) {
        this.classEngravingId = normalize(classEngravingId);
    }

    public void equipCommonEngraving(int slotNo, EquippedCommonEngraving value) {
        commonEngravings.put(slotNo, value);
    }

    public void unequipCommonEngraving(int slotNo) {
        commonEngravings.remove(slotNo);
    }

    public Map<Integer, EquippedCommonEngraving> commonEngravings() {
        return Map.copyOf(new LinkedHashMap<>(commonEngravings));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    public record EquippedCommonEngraving(
            String engravingId,
            int level
    ) {
        public EquippedCommonEngraving {
            engravingId = engravingId == null ? "" : engravingId.trim().toLowerCase(Locale.ROOT);
            level = Math.max(1, level);
        }
    }
}
