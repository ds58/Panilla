package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public interface NbtCheck {

	String getName();

	default String[] getAliases() {
		return new String[] {};
	}

	boolean check(INbtTagCompound tag, String nmsItemClassName);

}
