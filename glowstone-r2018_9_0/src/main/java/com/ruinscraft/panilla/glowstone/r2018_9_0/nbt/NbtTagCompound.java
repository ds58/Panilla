package com.ruinscraft.panilla.glowstone.r2018_9_0.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.TagType;

import java.util.Set;

public class NbtTagCompound implements INbtTagCompound {

    private final CompoundTag handle;

    public NbtTagCompound(CompoundTag handle) {
        this.handle = handle;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public boolean hasKey(String key) {
        return handle.containsKey(key);
    }

    @Override
    public boolean hasKeyOfType(String key, NbtDataType nbtDataType) {
        switch (nbtDataType) {
            case END:
                return handle.isEmpty();    // TODO: no
            case BYTE:
                return handle.isByte(key);
            case SHORT:
                return handle.isShort(key);
            case INT:
                return handle.isInt(key);
            case LONG:
                return handle.isLong(key);
            case FLOAT:
                return handle.isFloat(key);
            case DOUBLE:
                return handle.isDouble(key);
            case BYTE_ARRAY:
                return handle.isByteArray(key);
            case STRING:
                return handle.isString(key);
            case LIST:
                return handle.isCompoundList(key);
            case COMPOUND:
                return handle.isCompound(key);
            case INT_ARRAY:
                return handle.isIntArray(key);
            default:
                return false;
        }
    }

    @Override
    public Set<String> getKeys() {
        return handle.getValue().keySet();
    }

    @Override
    public int getInt(String key) {
        return handle.getInt(key);
    }

    @Override
    public short getShort(String key) {
        return handle.getShort(key);
    }

    @Override
    public String getString(String key) {
        return handle.getString(key);
    }

    @Override
    public INbtTagList getList(String key, NbtDataType nbtDataType) {
        return new NbtTagList(handle.getList(key, TagType.byId(nbtDataType.id)));
    }

    @Override
    public INbtTagCompound getCompound(String key) {
        return new NbtTagCompound(handle.getCompound(key));
    }

}
