package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

public interface IPacketInspector {

	/* generally all packets */
	void checkSize(Object player, Object nmsPacket) throws OversizedPacketException;
	/* inbound packets (client->server) */
	void checkPacketPlayInSetCreativeSlot(Object player, Object nmsPacket) throws Exception;
	void checkPacketPlayInWindowClick(Object player, Object nmsPacket) throws Exception;	// TODO: should we keep this?
	void checkPacketPlayInUpdateSign(Object player, Object nmsPacket) throws SignLineLengthTooLongException;
	/* outbound packets (server->client) */
	void checkPacketPlayOutSetSlot(Object player, Object nmsPacket) throws Exception;

	default void checkIn(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
		checkPacketPlayInSetCreativeSlot(player, nmsPacket);
		checkPacketPlayInWindowClick(player, nmsPacket);
		checkPacketPlayInUpdateSign(player, nmsPacket);
	}
	
	default void checkOut(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
		checkPacketPlayOutSetSlot(player, nmsPacket);
	}
	
}
