package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

public interface IPacketInspector {

    /* all packets */
    void checkSize(Object _packet, boolean from) throws OversizedPacketException;

    /* inbound packets (client->server) */
    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    void checkPacketPlayInUpdateSign(Object _packet) throws SignLineLengthTooLongException;

    /* outbound packets (server->client) */
    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    default void checkPlayIn(Object _packet) throws PacketException {
        checkSize(_packet, true);
        checkPacketPlayInSetCreativeSlot(_packet);
        checkPacketPlayInUpdateSign(_packet);
    }

    default void checkPlayOut(Object _packet) throws PacketException {
        checkSize(_packet, false);
        checkPacketPlayOutSetSlot(_packet);
    }

}
