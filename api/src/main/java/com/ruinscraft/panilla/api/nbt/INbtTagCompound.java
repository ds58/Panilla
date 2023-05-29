package com.ruinscraft.panilla.api.nbt;

import java.util.Set;

public interface INbtTagCompound {

    boolean hasKey(String key);

    boolean hasKeyOfType(String key, NbtDataType nbtDataType);

    Set<String> getKeys();

    int getInt(String key);

    short getShort(String key);

    String getString(String key);

    INbtTagList getList(String key, NbtDataType nbtDataType);

    INbtTagCompound getCompound(String key);

}
