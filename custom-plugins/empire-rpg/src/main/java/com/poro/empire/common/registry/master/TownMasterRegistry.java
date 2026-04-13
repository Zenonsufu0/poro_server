package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.TownMaster;

public final class TownMasterRegistry extends AbstractMasterRegistry<TownMaster> {
    public TownMasterRegistry() {
        super(TownMaster::townId);
    }
}
