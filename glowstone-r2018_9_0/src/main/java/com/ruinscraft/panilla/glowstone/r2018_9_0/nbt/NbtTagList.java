package com.ruinscraft.panilla.glowstone.r2018_9_0.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import net.glowstone.util.nbt.ListTag;
import net.glowstone.util.nbt.Tag;
import net.glowstone.util.nbt.TagType;

public class NbtTagList<T extends Tag> implements INbtTagList {

    private ListTag<T> handle;

    public NbtTagList(ListTag<T> handle) {
        this.handle = handle;
    }

    @Override
    public INbtTagCompound getCompound(int index) {
        if (handle.getChildType() != TagType.COMPOUND) {
            return null;    // probably safe...
        }
        return (INbtTagCompound) handle.getValue().get(index);
    }

    @Override
    public String getString(int index) {
        return handle.getValue().get(index).toString();
    }

    @Override
    public int size() {
        return handle.getValue().size();
    }

}
