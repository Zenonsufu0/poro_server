package com.poro.empire.common.registry.master;

import com.poro.empire.common.registry.master.model.SkillMaster;

public final class SkillMasterRegistry extends AbstractMasterRegistry<SkillMaster> {
    public SkillMasterRegistry() {
        super(SkillMaster::skillId);
    }
}
