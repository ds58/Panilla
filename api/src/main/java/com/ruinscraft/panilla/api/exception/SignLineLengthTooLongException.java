package com.ruinscraft.panilla.api.exception;

public class SignLineLengthTooLongException extends PacketException {

    private static final long serialVersionUID = -4895566449016462848L;

    public SignLineLengthTooLongException(String nmsClass, boolean from) {
        super(nmsClass, from);
    }

}
