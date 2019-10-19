package com.ruinscraft.panilla.api.exception;

import java.util.UUID;

public class EntityNbtNotPermittedException extends PacketException {

    private final UUID entityId;

    public EntityNbtNotPermittedException(String nmsClass, boolean from, UUID entityId) {
        super(nmsClass, from);
        this.entityId = entityId;
    }

    public UUID getEntityId() {
        return entityId;
    }

}
