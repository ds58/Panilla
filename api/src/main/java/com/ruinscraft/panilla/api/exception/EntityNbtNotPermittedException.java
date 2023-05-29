package com.ruinscraft.panilla.api.exception;

import java.util.UUID;

public class EntityNbtNotPermittedException extends PacketException {

    private final UUID entityId;

    public EntityNbtNotPermittedException(String nmsClass, boolean from, FailedNbt failedNbt, UUID entityId) {
        super(nmsClass, from, failedNbt);
        this.entityId = entityId;
    }

    public UUID getEntityId() {
        return entityId;
    }

}
