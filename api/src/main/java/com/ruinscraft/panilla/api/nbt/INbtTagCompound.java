package com.ruinscraft.panilla.api.nbt;

import java.util.Set;

public interface INbtTagCompound {

	boolean hasKey(String key);
	Set<String> getKeys();
	int getInt(String key);
	short getShort(String key);
	String getString(String key);
	INbtTagList getList(String key, NbtDataType nbtDataType);
	INbtTagCompound getCompound(String key);
	
}
