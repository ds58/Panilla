package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.io.PacketRateLimiter;

import java.util.Set;

public interface IPanillaPlayer {

    Object getHandle();

    String getName();

    String getCurrentWorldName();

    boolean hasPermission(String node);

    boolean canBypassChecks(IPanilla panilla, PacketException e);

    Set<PacketRateLimiter> getRateLimiters();

    void kick(String message);

}
