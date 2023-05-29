package com.ruinscraft.panilla.api;

/*
 * Minecraft Constants
 * 
 * Made up constants should be prefixed with NOT_PROTOCOL_
 * and be a reasonable value which would prevent game-breaking things.
 */
public interface IProtocolConstants {
	
	/* Values which could potentially change or have changed in previous versions of Minecraft */
	
	int packetMaxBytes();
	
	int maxAnvilRenameChars();

	int maxSignLineLength();

	/* Default values that I don't expect to change in future versions of Minecraft */
	
	default int fireworksMaxFlight() {
		return 3;
	}
	
	default int fireworksMinFlight() {
		return 1;
	}
	
	default int fireworksMaxExplosions() {
		return 8;
	}
	
	default int minStackSize() {
		return 1;
	}
	
	/* Not protocol -- Reasonable values/assumptions */
	
	default int NOT_PROTOCOL_maxLoreLineLength() {
		return 128;
	}
	
}
