package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;
import org.bukkit.entity.Player;

public interface IPacketInspector {

    /* all packets */
    void checkSize(Object _packet, boolean from) throws OversizedPacketException;

    /* inbound packets (client->server) */
    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    /* outbound packets (server->client) */
    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(Player player, int slot);

    default void checkPlayIn(Player player, Object _packet) throws PacketException {
        checkSize(_packet, true);
        try {
            checkPacketPlayInSetCreativeSlot(_packet);
        } catch (NbtNotPermittedException e) {
            sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            throw e;
        }
    }

    default void checkPlayOut(Player player, Object _packet) throws PacketException {
        checkSize(_packet, false);
        checkPacketPlayOutSetSlot(_packet);
    }

}
