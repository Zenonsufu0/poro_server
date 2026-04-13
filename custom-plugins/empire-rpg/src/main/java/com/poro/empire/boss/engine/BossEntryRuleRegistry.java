package com.poro.empire.boss.engine;

import com.poro.empire.common.registry.InMemoryRegistry;

import java.util.Map;
import java.util.Optional;

public final class BossEntryRuleRegistry {
    private final InMemoryRegistry<String, BossEntryRule> delegate = new InMemoryRegistry<>();

    public void register(BossEntryRule rule) {
        delegate.register(rule.bossId(), rule);
    }

    public Optional<BossEntryRule> find(String bossId) {
        return delegate.find(bossId == null ? "" : bossId.trim().toLowerCase());
    }

    public Map<String, BossEntryRule> all() {
        return delegate.snapshot();
    }
}
