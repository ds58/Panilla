package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_SkullOwner extends NbtCheck {

	public NbtCheck_SkullOwner() {
		super("SkullOwner", PStrictness.AVERAGE);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PStrictness strictness) {
		return true;
	}

}
