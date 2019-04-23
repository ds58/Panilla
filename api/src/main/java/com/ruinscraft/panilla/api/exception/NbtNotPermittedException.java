package com.ruinscraft.panilla.api.exception;

public class NbtNotPermittedException extends PacketException {

    private static final long serialVersionUID = 4005240262520128653L;

    private final String tagName;

    public NbtNotPermittedException(String nmsClass, boolean from, String tagName) {
        super(nmsClass, from);
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

}
