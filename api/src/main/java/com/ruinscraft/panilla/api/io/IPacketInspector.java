package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

public interface IPacketInspector {

	/* generally all packets */
	void checkSize(Object _player, Object _packet) throws OversizedPacketException;
	/* inbound packets (client->server) */
	void checkPacketPlayInSetCreativeSlot(Object _player, Object _packet) throws NbtNotPermittedException;
	void checkPacketPlayInUpdateSign(Object _player, Object _packet) throws SignLineLengthTooLongException;
	/* outbound packets (server->client) */
	void checkPacketPlayOutSetSlot(Object _player, Object _packet) throws NbtNotPermittedException;

	default void checkIn(Object _player, Object _packet) throws OversizedPacketException, NbtNotPermittedException, SignLineLengthTooLongException {
		checkSize(_player, _packet);
		checkPacketPlayInSetCreativeSlot(_player, _packet);
		checkPacketPlayInUpdateSign(_player, _packet);
	}
	
	default void checkOut(Object _player, Object _packet) throws OversizedPacketException, NbtNotPermittedException, SignLineLengthTooLongException {
		checkSize(_player, _packet);
		checkPacketPlayOutSetSlot(_player, _packet);
	}
	
}
