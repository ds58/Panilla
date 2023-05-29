package com.ruinscraft.panilla.api;

public class UnsupportedMinecraftVersionException extends Exception {

	private static final long serialVersionUID = 6254811630682579119L;

	private final String v_Version;
	
	public UnsupportedMinecraftVersionException(String v_Version) {
		this.v_Version = v_Version;
	}
	
	@Override
	public String getMessage() {
		return String.format("The Minecraft version '%s' is not supported.", v_Version);
	}
	
}
