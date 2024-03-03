package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.LegacyEntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.PacketException;

import java.util.UUID;

public interface IPacketInspector {

    void checkPacketPlayInClickContainer(Object packetHandle) throws NbtNotPermittedException;

    void checkPacketPlayInSetCreativeSlot(Object packetHandle) throws NbtNotPermittedException;

    /**
     * Checks PacketPlayOutSetSlot
     * Should optionally return (depending on config) if the window id is not 0
     *
     * @param packetHandle
     * @throws NbtNotPermittedException
     */
    void checkPacketPlayOutSetSlot(Object packetHandle) throws NbtNotPermittedException;

    /**
     * Checks PacketPlayOutWindowItems
     * Should return if the window id is not 0 (the player's inventory)
     *
     * @param packetHandle
     * @throws NbtNotPermittedException
     */
    void checkPacketPlayOutWindowItems(Object packetHandle) throws NbtNotPermittedException;

    void checkPacketPlayOutSpawnEntity(Object packetHandle) throws EntityNbtNotPermittedException, LegacyEntityNbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot);

    void stripNbtFromItemEntity(UUID entityId);

    void stripNbtFromItemEntityLegacy(int entityId);

    void validateBaseComponentParse(String string) throws Exception;

    default void checkPlayIn(IPanilla panilla, IPanillaPlayer player, Object packetHandle) throws PacketException {
        try {
            checkPacketPlayInClickContainer(packetHandle);
        } catch (NbtNotPermittedException e) {
            if (!player.canBypassChecks(panilla, e)) {
                sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            }
            throw e;
        }

        try {
            checkPacketPlayInSetCreativeSlot(packetHandle);
        } catch (NbtNotPermittedException e) {
            if (!player.canBypassChecks(panilla, e)) {
                sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            }
            throw e;
        }
    }

    default void checkPlayOut(IPanilla panilla, Object packetHandle) throws PacketException {
        checkPacketPlayOutSetSlot(packetHandle);
        checkPacketPlayOutWindowItems(packetHandle);

        try {
            checkPacketPlayOutSpawnEntity(packetHandle);
        } catch (EntityNbtNotPermittedException e) {
            if (!panilla.getPConfig().disabledWorlds.contains(e.getWorld())) {
                stripNbtFromItemEntity(e.getEntityId());
                panilla.getPanillaLogger().log(panilla.getPTranslations().getTranslation("itemEntityStripped", e.getFailedNbt().key), true);
                throw e;
            }
        } catch (LegacyEntityNbtNotPermittedException e) {
            stripNbtFromItemEntityLegacy(e.getEntityId());
            panilla.getPanillaLogger().log(panilla.getPTranslations().getTranslation("itemEntityStripped", e.getFailedNbt().key), true);
            throw e;
        }
    }

}
