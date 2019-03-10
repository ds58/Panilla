package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.exception.OversizedPacketException;

public interface IPacketInspector {

	void checkSize(Object nmsPacket) throws OversizedPacketException;
	void checkPacketPlayInSetCreativeSlot(Object nmsPacket);

	default void checkIn(Object nmsPacket) throws Exception {
		checkSize(nmsPacket);
		checkPacketPlayInSetCreativeSlot(nmsPacket);
	}
	
	default void checkOut(Object nmsPacket) throws Exception {
		checkSize(nmsPacket);
	}
	
}
