package com.ruinscraft.panilla.api.exception;

public class PacketException extends Exception {

    private final String nmsClass;
    private final boolean from;

    public PacketException(String nmsClass, boolean from) {
        this.nmsClass = nmsClass;
        this.from = from;
    }

    public String getNmsClass() {
        return nmsClass;
    }

    // is from client
    public boolean isFrom() {
        return from;
    }

}
