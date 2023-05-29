package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.PacketException;

public interface IPacketInspector {

    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot);

    default void checkPlayIn(IPanillaPlayer player, Object _packet) throws PacketException {
        try {
            checkPacketPlayInSetCreativeSlot(_packet);
        } catch (NbtNotPermittedException e) {
            sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            throw e;
        }
    }

    default void checkPlayOut(Object _packet) throws PacketException {
        checkPacketPlayOutSetSlot(_packet);
    }

}
