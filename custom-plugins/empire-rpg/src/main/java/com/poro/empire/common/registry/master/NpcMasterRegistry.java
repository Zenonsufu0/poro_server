package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.NpcMaster;

public final class NpcMasterRegistry extends AbstractMasterRegistry<NpcMaster> {
    public NpcMasterRegistry() {
        super(NpcMaster::npcId);
    }
}
