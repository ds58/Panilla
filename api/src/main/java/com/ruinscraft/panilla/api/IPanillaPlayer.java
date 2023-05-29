package com.ruinscraft.panilla.api;

public interface IPanillaPlayer {

    Object getHandle();

    String getName();

    boolean hasPermission(String node);

    void sendMessage(String message);

    default boolean canBypassChecks() {
        return hasPermission(IPanilla.PERMISSION_BYPASS);
    }

}
