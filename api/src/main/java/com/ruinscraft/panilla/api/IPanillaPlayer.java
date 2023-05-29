package com.ruinscraft.panilla.api;

public interface IPanillaPlayer {

    Object getHandle();

    String getName();

    boolean isOp();

    boolean hasPermission(String node);

    void sendMessage(String message);

}
