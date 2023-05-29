package com.ruinscraft.panilla.glowstone.r2018_9_0.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;

import java.util.List;

public class NbtTagList<V> implements INbtTagList {

    private final List<V> handle;

    public NbtTagList(List<V> handle) {
        this.handle = handle;
    }

    @Override
    public INbtTagCompound getCompound(int index) {
        return null;
    }

    @Override
    public String getString(int index) {
        return handle.get(index).toString();
    }

    @Override
    public int size() {
        return handle.size();
    }

}
