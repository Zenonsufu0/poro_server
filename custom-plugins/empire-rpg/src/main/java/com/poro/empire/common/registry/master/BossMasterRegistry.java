package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.BossMaster;

public final class BossMasterRegistry extends AbstractMasterRegistry<BossMaster> {
    public BossMasterRegistry() {
        super(BossMaster::bossId);
    }
}
