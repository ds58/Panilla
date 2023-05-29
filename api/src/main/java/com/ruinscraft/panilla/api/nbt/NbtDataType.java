package com.ruinscraft.panilla.api.nbt;

public enum NbtDataType {

	END(0x00), BYTE(0x01), SHORT(0x02), INT(0x03), LONG(0x04), FLOAT(0x05), DOUBLE(0x06), BYTE_ARRAY(0x07),
	STRING(0x08), LIST(0x09), COMPOUND(0x10), INT_ARRAY(0x11);

	public final int id;

	NbtDataType(int id) {
		this.id = id;
	}

}
