package com.ruinscraft.panilla.paper.v1_21_5.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import de.tr7zw.changeme.nbtapi.NBTType;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;

import java.util.Collections;
import java.util.Set;

public class NbtTagCompound implements INbtTagCompound {

    private final ReadWriteNBT handle;

    public NbtTagCompound(ReadWriteNBT handle) {
        this.handle = handle;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public boolean hasKey(String key) {
        if (handle == null) return false;
        return handle.hasTag(key);
    }

    @Override
    public boolean hasKeyOfType(String key, NbtDataType nbtDataType) {
        if (handle == null) return false;
        return handle.hasTag(key, NBTType.valueOf(nbtDataType.id));
    }

    @Override
    public Set<String> getKeys() {
        if (handle == null) return Collections.emptySet();
        return handle.getKeys();
    }

    @Override
    public int getInt(String key) {
        return handle.getInteger(key);
    }

    @Override
    public double getDouble(String key) {
        return handle.getDouble(key);
    }

    @Override
    public short getShort(String key) {
        return handle.getShort(key);
    }

    @Override
    public byte getByte(String key) {
        return handle.getByte(key);
    }

    @Override
    public String getString(String key) {
        return handle.getString(key);
    }

    @Override
    public int[] getIntArray(String key) {
        return handle.getIntArray(key);
    }

    @Override
    public INbtTagList getList(String key, NbtDataType nbtDataType) {
        return new NbtTagList(nbtDataType == NbtDataType.STRING ? handle.getStringList(key) : handle.getCompoundList(key));
    }

    @Override
    public INbtTagList getList(String key) {
        return new NbtTagList(handle.getCompoundList(key));
    }

    @Override
    public INbtTagCompound getCompound(String key) {
        return new NbtTagCompound(handle.getCompound(key));
    }

}
