package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.OversizedPacketException;

public interface IPacketInspector {

	void checkSize(Object nmsPacket) throws OversizedPacketException;
	
}
