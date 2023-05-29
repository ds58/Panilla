package com.ruinscraft.panilla.api;

public interface IPanillaPlayer {

    Object getHandle();

    String getName();

    String getCurrentWorldName();

    boolean hasPermission(String node);

    void sendMessage(String message);

    boolean canBypassChecks();

}
