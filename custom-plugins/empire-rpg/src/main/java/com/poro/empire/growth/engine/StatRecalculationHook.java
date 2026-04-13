package com.poro.empire.growth.engine;

public interface StatRecalculationHook {
    void onRecalculate(PlayerGrowthState state);
}
