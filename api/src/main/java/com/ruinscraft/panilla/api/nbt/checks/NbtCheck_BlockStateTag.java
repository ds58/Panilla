package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_BlockStateTag implements NbtCheck {

	@Override
	public String getName() {
		return "BlockStateTag";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		return true;	// TODO: implement
	}
	
}
