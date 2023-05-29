package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_title extends NbtCheck {

	public NbtCheck_title() {
		super("title", PStrictness.AVERAGE);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PStrictness strictness) {
		int titleLength = tag.getString("title").length();

		if (titleLength > protocolConstants.maxBookTitleLength()) {
			return false;
		}

		return true;
	}

}
