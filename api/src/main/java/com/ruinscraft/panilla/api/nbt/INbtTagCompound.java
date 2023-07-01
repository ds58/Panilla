package com.ruinscraft.panilla.api.nbt;

import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public interface INbtTagCompound {

    Object getHandle();

    boolean hasKey(String key);

    boolean hasKeyOfType(String key, NbtDataType nbtDataType);

    Set<String> getKeys();

    default Set<String> getNonMinecraftKeys() {
        Set<String> defaultKeys = NbtChecks.getChecks().keySet();
        Set<String> nonMinecraftKeys = new HashSet<>();

        for (String key : getKeys()) {
            if (!defaultKeys.contains(key)) {
                nonMinecraftKeys.add(key);
            }
        }

        return nonMinecraftKeys;
    }

    int getInt(String key);

    double getDouble(String key);

    short getShort(String key);

    String getString(String key);

    int[] getIntArray(String key);

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
