package com.ruinscraft.panilla.craftbukkit.v1_19_R3.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
        return handle.e(key);
    }

    @Override
    public boolean hasKeyOfType(String key, NbtDataType nbtDataType) {
        return handle.b(key, nbtDataType.id);
    }

    @Override
    public Set<String> getKeys() {
        return handle.e();
    }

    @Override
    public int getInt(String key) {
        return handle.h(key);
    }

    @Override
    public double getDouble(String key) {
        return handle.k(key);
    }

    @Override
    public short getShort(String key) {
        return handle.g(key);
    }

    @Override
    public String getString(String key) {
        return handle.l(key);
    }

    @Override
    public int[] getIntArray(String key) {
        return handle.n(key);
    }

    @Override
    public INbtTagList getList(String key, NbtDataType nbtDataType) {
        return new NbtTagList(handle.c(key, nbtDataType.id));
    }

    @Override
    public INbtTagList getList(String key) {
        NBTBase base = handle.c(key);

        if (base instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) base;
            return new NbtTagList(list);
        }

        return null;
    }

    @Override
    public INbtTagCompound getCompound(String key) {
        return new NbtTagCompound(handle.p(key));
    }

}
