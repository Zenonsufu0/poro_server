package com.poro.empire.growth.engine;

public interface PotentialSelectionHook {
    PotentialProfile choose(
            String operationType,
            PlayerGrowthState state,
            PlayerEquipmentItem item,
            PotentialProfile before,
            PotentialProfile after
    );
}
