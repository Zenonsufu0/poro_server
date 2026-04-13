package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.ItemMaster;

public final class ItemMasterRegistry extends AbstractMasterRegistry<ItemMaster> {
    public ItemMasterRegistry() {
        super(ItemMaster::itemId);
    }
}
