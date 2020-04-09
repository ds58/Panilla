package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.LegacyEntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.PacketException;

import java.util.UUID;

public interface IPacketInspector {

    void checkPacketPlayInSetCreativeSlot(Object packetHandle) throws NbtNotPermittedException;

    void checkPacketPlayOutSetSlot(Object packetHandle) throws NbtNotPermittedException;

    void checkPacketPlayOutSpawnEntity(Object packetHandle) throws EntityNbtNotPermittedException, LegacyEntityNbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot);

    void removeEntity(UUID entityId);

    @Deprecated
    void removeEntityLegacy(int entityId);

    void validateBaseComponentParse(String string) throws Exception;

    default void checkPlayIn(IPanilla panilla, IPanillaPlayer player, Object packetHandle) throws PacketException {
        try {
            checkPacketPlayInSetCreativeSlot(packetHandle);
        } catch (NbtNotPermittedException e) {
            if (!player.canBypassChecks(panilla, e)) {
                sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            }
            throw e;
        }
    }

    default void checkPlayOut(Object packetHandle) throws PacketException {
        checkPacketPlayOutSetSlot(packetHandle);
        try {
            checkPacketPlayOutSpawnEntity(packetHandle);
        } catch (EntityNbtNotPermittedException e) {
            removeEntity(e.getEntityId());
            throw e;
        } catch (LegacyEntityNbtNotPermittedException e) {
            removeEntityLegacy(e.getEntityId());
            throw e;
        }
    }

}
