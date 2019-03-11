package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

public interface IPacketInspector {

	void checkSize(Object player, Object nmsPacket) throws OversizedPacketException;
	void checkPacketPlayInSetCreativeSlot(Object player, Object nmsPacket) throws NbtNotPermittedException;
	void checkPacketPlayInUpdateSign(Object player, Object nmsPacket) throws SignLineLengthTooLongException;

	default void checkIn(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
		checkPacketPlayInSetCreativeSlot(player, nmsPacket);
		checkPacketPlayInUpdateSign(player, nmsPacket);
	}
	
	default void checkOut(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
	}
	
}
