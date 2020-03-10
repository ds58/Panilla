package com.ruinscraft.panilla.api.nbt;

import java.io.UnsupportedEncodingException;
import java.util.Set;

public interface INbtTagCompound {

    Object getHandle();

    boolean hasKey(String key);

    boolean hasKeyOfType(String key, NbtDataType nbtDataType);

    Set<String> getKeys();

    int getInt(String key);

    short getShort(String key);

    String getString(String key);

    INbtTagList getList(String key, NbtDataType nbtDataType);

    INbtTagList getList(String key);

    INbtTagCompound getCompound(String key);

    default int getStringSizeBytes() {
        try {
            return getHandle().toString().getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
