package com.ruinscraft.panilla.api.exception;

public class OversizedPacketException extends PacketException {

	private static final long serialVersionUID = -4128984869675949399L;

	private final int sizeBytes;

	public OversizedPacketException(String nmsClass, boolean from, int sizeBytes) {
		super(nmsClass, from);
		this.sizeBytes = sizeBytes;
	}

	public int getSizeBytes() {
		return sizeBytes;
	}

}
