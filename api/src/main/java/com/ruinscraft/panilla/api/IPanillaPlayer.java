package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.PacketException;

public interface IPanillaPlayer {

    Object getHandle();

    String getName();

    String getCurrentWorldName();

    boolean hasPermission(String node);

    boolean canBypassChecks(IPanilla panilla, PacketException e);

}
