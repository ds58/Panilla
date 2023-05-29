package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionEffects implements NbtCheck {

	@Override
	public String getName() {
		return "CustomPotionEffects";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		if (tag.hasKey(getName())) return false;
		return true;
	}

}
