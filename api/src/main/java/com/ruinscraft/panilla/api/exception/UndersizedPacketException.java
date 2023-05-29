package com.ruinscraft.panilla.api.exception;

public class UndersizedPacketException extends OversizedPacketException {

    public UndersizedPacketException(String nmsClass, boolean from, int sizeBytes) {
        super(nmsClass, from, sizeBytes);
    }

}
