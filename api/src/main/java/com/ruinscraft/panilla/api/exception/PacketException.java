package com.ruinscraft.panilla.api.exception;

public class PacketException extends Exception {

    private final String nmsClass;
    private final boolean from;
    private final FailedNbt failedNbt;

    public PacketException(String nmsClass, boolean from, FailedNbt failedNbt) {
        this.nmsClass = nmsClass;
        this.from = from;
        this.failedNbt = failedNbt;
    }

    public String getNmsClass() {
        return nmsClass;
    }

    // is from client
    public boolean isFrom() {
        return from;
    }

    public FailedNbt getFailedNbt() {
        return failedNbt;
    }

}
