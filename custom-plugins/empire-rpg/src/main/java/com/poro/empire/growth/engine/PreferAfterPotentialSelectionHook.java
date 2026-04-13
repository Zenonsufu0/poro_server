package com.poro.empire.growth.engine;

public final class PreferAfterPotentialSelectionHook implements PotentialSelectionHook {
    @Override
    public PotentialProfile choose(
            String operationType,
            PlayerGrowthState state,
            PlayerEquipmentItem item,
            PotentialProfile before,
            PotentialProfile after
    ) {
        return after;
    }
}
