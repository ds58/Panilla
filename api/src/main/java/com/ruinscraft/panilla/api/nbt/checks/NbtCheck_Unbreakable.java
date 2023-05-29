package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_Unbreakable extends NbtCheck {

	public NbtCheck_Unbreakable() {
		super("Unbreakable", PStrictness.LENIENT);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
		return false;
	}

}
