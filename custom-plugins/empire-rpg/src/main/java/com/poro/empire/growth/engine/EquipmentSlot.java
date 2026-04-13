package com.poro.empire.growth.engine;

import java.util.Locale;

public enum EquipmentSlot {
    WEAPON("weapon"),
    ARMOR_HEAD("armor"),
    ARMOR_CHEST("armor"),
    ARMOR_HANDS("armor"),
    ARMOR_LEGS("armor"),
    ACCESSORY_1("accessory"),
    ACCESSORY_2("accessory"),
    ACCESSORY_3("accessory");

    private final String itemSlotType;

    EquipmentSlot(String itemSlotType) {
        this.itemSlotType = itemSlotType;
    }

    public String itemSlotType() {
        return itemSlotType;
    }

    public static EquipmentSlot from(String raw) {
        return valueOf(raw.trim().toUpperCase(Locale.ROOT));
    }
}
