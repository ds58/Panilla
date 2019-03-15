package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_author extends NbtCheck {

	public NbtCheck_author() {
		super("author", PStrictness.AVERAGE);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PStrictness strictness) {
		int authorLength = tag.getString("author").length();

		if (authorLength > protocolConstants.maxUsernameLength()) {
			return false;
		}

		return true;

	}

}
