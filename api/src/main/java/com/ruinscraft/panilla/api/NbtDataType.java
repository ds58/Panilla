package com.ruinscraft.panilla.api;

public enum NbtDataType {

	END(0),
	BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	BYTE_ARRAY(7),
	STRING(8),
	LIST(9),
	COMPOUND(10),
	INT_ARRAY(11);
	
	final int id;
	
	NbtDataType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
