package com.ruinscraft.panilla.craftbukkit.v1_8_R3.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import java.util.Set;

public class NbtTagCompound implements INbtTagCompound {

    private final NBTTagCompound handle;

    public NbtTagCompound(NBTTagCompound handle) {
        this.handle = handle;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public boolean hasKey(String key) {
        return handle.hasKey(key);
    }

    @Override
    public boolean hasKeyOfType(String key, NbtDataType nbtDataType) {
        return handle.hasKeyOfType(key, nbtDataType.id);
    }

    @Override
    public Set<String> getKeys() {
        return handle.c();
    }

    @Override
    public int getInt(String key) {
        return handle.getInt(key);
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
        return new NbtTagList(handle.getList(key, nbtDataType.id));
    }

    @Override
    public INbtTagList getList(String key) {
        NBTBase base = handle.get(key);

        if (base instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) base;
            return new NbtTagList(list);
        }

        return null;
    }

    @Override
    public INbtTagCompound getCompound(String key) {
        return new NbtTagCompound(handle.getCompound(key));
    }

}
