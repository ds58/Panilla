package com.ruinscraft.panilla.api;

public interface IProtocolConstants {

	int packetMaxBytes();
	
	int maxAnvilRenameChars();

	default int NOT_PROTOCOL_maxLoreLineLength() {
		return 128;
	}
	
}
