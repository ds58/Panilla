package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;
import io.netty.buffer.ByteBuf;

public interface IPacketInspector {

    int getPacketSize(ByteBuf packetBuf);

    /* all packets */
    default void checkSize(ByteBuf packetBuf, Object _packet, boolean from, IPanilla panilla) throws OversizedPacketException {
        if (panilla.getProtocolConstants().packetCompressionLevel() <= 0) {
            return; // no compression
        }

        int packetSize = getPacketSize(packetBuf);

//        if (packetSize < panilla.getProtocolConstants().packetCompressionLevel()) {
//            throw new UndersizedPacketException(_packet.getClass().getSimpleName(), from, packetSize);
//        }

        if (packetSize > panilla.getProtocolConstants().maxPacketSizeBytes()) {
            throw new OversizedPacketException(_packet.getClass().getSimpleName(), from, packetSize);
        }
    }

    /* inbound packets (client->server) */
    void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException;

    /* outbound packets (server->client) */
    void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException;

    void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot);

    default void checkPlayIn(IPanillaPlayer player, Object _packet, IPanilla panilla) throws PacketException {
        checkSize(_packet, true, panilla);
        try {
            checkPacketPlayInSetCreativeSlot(_packet);
        } catch (NbtNotPermittedException e) {
            sendPacketPlayOutSetSlotAir(player, e.getItemSlot());
            throw e;
        }
    }

    default void checkPlayOut(IPanillaPlayer player, Object _packet, IPanilla panilla) throws PacketException {
        checkSize(_packet, false, panilla);
        checkPacketPlayOutSetSlot(_packet);
    }

}
