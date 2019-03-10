package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;

public interface IPacketInspector {

	void checkSize(Object player, Object nmsPacket) throws OversizedPacketException;
	void checkPacketPlayInSetCreativeSlot(Object player, Object nmsPacket) throws NbtNotPermittedException;

	default void checkIn(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
		checkPacketPlayInSetCreativeSlot(player, nmsPacket);
	}
	
	default void checkOut(Object player, Object nmsPacket) throws Exception {
		checkSize(player, nmsPacket);
	}
	
}
