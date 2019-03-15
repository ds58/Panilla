package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CanPlaceOn implements NbtCheck {

	@Override
	public String getName() {
		return "CanPlaceOn";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		if (tag.hasKey(getName())) return false;
		return true;
	}
	
}
