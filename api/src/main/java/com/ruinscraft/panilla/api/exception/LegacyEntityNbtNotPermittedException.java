package com.ruinscraft.panilla.api.exception;

@Deprecated // for 1.8 use only
public class LegacyEntityNbtNotPermittedException extends PacketException {

    private final int entityId;

    public LegacyEntityNbtNotPermittedException(String nmsClass, boolean from, FailedNbt failedNbt, int entityId) {
        super(nmsClass, from, failedNbt);
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

}
