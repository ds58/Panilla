package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;

public interface IPacketInspector {

    /* all packets */
    void checkSize(Object _packet, boolean from) throws OversizedPacketException;

    /* inbound packets (client->server) */
    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    /* outbound packets (server->client) */
    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    default void checkPlayIn(Object _packet) throws PacketException {
        checkSize(_packet, true);
        checkPacketPlayInSetCreativeSlot(_packet);
    }

    default void checkPlayOut(Object _packet) throws PacketException {
        checkSize(_packet, false);
        checkPacketPlayOutSetSlot(_packet);
    }

}
