package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.RegionMaster;

public final class RegionMasterRegistry extends AbstractMasterRegistry<RegionMaster> {
    public RegionMasterRegistry() {
        super(RegionMaster::regionCode);
    }
}
