package com.ruinscraft.panilla.api.exception;

import java.util.UUID;

public class EntityNbtNotPermittedException extends PacketException {

    private final UUID entityId;
    private final String world;

    public EntityNbtNotPermittedException(String nmsClass, boolean from, FailedNbt failedNbt, UUID entityId, String world) {
        super(nmsClass, from, failedNbt);
        this.entityId = entityId;
        this.world = world;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public String getWorld() {
        return world;
    }

}
