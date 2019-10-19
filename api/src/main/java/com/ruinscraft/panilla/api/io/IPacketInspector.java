package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.LegacyEntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.PacketException;

import java.util.UUID;

public interface IPacketInspector {

    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    void checkPacketPlayOutSpawnEntity(Object _packet) throws EntityNbtNotPermittedException, LegacyEntityNbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot);

    void removeEntity(UUID entityId);

    @Deprecated
    void removeEntityLegacy(int entityId);

    default void checkPlayIn(IPanillaPlayer player, Object _packet) throws PacketException {
        try {
            checkPacketPlayInSetCreativeSlot(_packet);
        } catch (NbtNotPermittedException e) {
            if (!player.canBypassChecks()) {
                sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            }
            throw e;
        }
    }

    default void checkPlayOut(Object _packet) throws PacketException {
        checkPacketPlayOutSetSlot(_packet);
        try {
            checkPacketPlayOutSpawnEntity(_packet);
        } catch (EntityNbtNotPermittedException e) {
            removeEntity(e.getEntityId());
            throw e;
        } catch (LegacyEntityNbtNotPermittedException e) {
            removeEntityLegacy(e.getEntityId());
            throw e;
        }
    }

}
