package com.poro.empire.npc.citizens;

import com.poro.empire.common.result.Result;

public interface InteractionProfileHook {
    Result<Void> bind(CitizensNpcGateway gateway, CitizensNpcHandle npc, CitizensNpcSeed seed);
}

