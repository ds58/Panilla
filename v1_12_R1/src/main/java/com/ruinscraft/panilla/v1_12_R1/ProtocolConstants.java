package com.ruinscraft.panilla.v1_12_R1;

import com.ruinscraft.panilla.api.IProtocolConstants;

public class ProtocolConstants implements IProtocolConstants {

	@Override
	public int packetMaxBytes() {
		return 2097152;
	}

	@Override
	public int maxAnvilRenameChars() {
		return 35;
	}

	// TODO: fill in
	@Override
	public int maxSignLineLength() {
		return 0;
	}
	
}
