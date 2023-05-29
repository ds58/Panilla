package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CanDestroy implements NbtCheck {

	@Override
	public String getName() {
		return "CanDestroy";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		if (tag.hasKey(getName())) return false;
		return true;
	}
	
}
