package com.ruinscraft.panilla.api.nbt;

public interface INbtTagList {

    INbtTagCompound getCompound(int index);

    String getString(int index);

    boolean isCompound(int index);

    int size();

}
