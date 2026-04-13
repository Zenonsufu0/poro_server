package com.poro.empire.growth.engine;

public interface EnhancementLogHook {
    void onAttempt(EnhancementService.EnhancementResult result);
}
