package com.ruinscraft.panilla.v1_12_R1;

import com.ruinscraft.panilla.api.io.IProtocolConstants;

public class ProtocolConstants implements IProtocolConstants {

	@Override
	public int packetMaxBytes() {
		return 2097152;
	}
	
}
