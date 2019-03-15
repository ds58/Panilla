package com.ruinscraft.panilla.v1_12_R1.nbt;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;

import net.minecraft.server.v1_12_R1.NBTTagList;

public class NbtTagList implements INbtTagList {

	private final NBTTagList handle;

	public NbtTagList(NBTTagList handle) {
		this.handle = handle;
	}

	@Override
	public INbtTagCompound get(int index) {
		return new NbtTagCompound(handle.get(index));
	}

	@Override
	public int size() {
		return handle.size();
	}
	
}
