package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_RepairCost implements NbtCheck {

	@Override
	public String getName() {
		return "RepairCost";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		return true;
	}
	
}
