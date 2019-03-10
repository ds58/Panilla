package com.ruinscraft.panilla.api.exception;

public class OversizedPacketException extends Exception {

	private static final long serialVersionUID = -4128984869675949399L;

	private String className;
	private int sizeBytes;
	
	public OversizedPacketException(String className, int sizeBytes) {
		this.className = className;
		this.sizeBytes = sizeBytes;
	}
	
	@Override
	public String getMessage() {
		return "packet class " + className + " was too large: " + sizeBytes;
	}
	
}
