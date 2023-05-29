package com.ruinscraft.panilla.api.nbt;

public interface INbtTagCompound {

	boolean hasKey(String key);
	int getInt(String key);
	short getShort(String key);
	String getString(String key);
	INbtTagList getList(String key, NbtDataType nbtDataType);
	INbtTagCompound getCompound(String key);
	
}
