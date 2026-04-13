package com.poro.empire.growth.engine;

import java.util.Locale;

public final class PlayerEquipmentItem {
    private final String itemInstanceId;
    private final String itemId;
    private int enhanceLevel;
    private PotentialProfile potentialProfile;

    public PlayerEquipmentItem(String itemInstanceId, String itemId) {
        this.itemInstanceId = normalize(itemInstanceId);
        this.itemId = normalize(itemId);
    }

    public String itemInstanceId() {
        return itemInstanceId;
    }

    public String itemId() {
        return itemId;
    }

    public int enhanceLevel() {
        return enhanceLevel;
    }

    public PotentialProfile potentialProfile() {
        return potentialProfile;
    }

    void setEnhanceLevel(int enhanceLevel) {
        this.enhanceLevel = Math.max(0, enhanceLevel);
    }

    void setPotentialProfile(PotentialProfile potentialProfile) {
        this.potentialProfile = potentialProfile;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
