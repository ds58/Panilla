package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_Potion implements NbtCheck {

	@Override
	public String getName() {
		return "Potion";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		return true;	// TODO: implement
	}
	
}
