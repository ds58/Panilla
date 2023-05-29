package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_display implements NbtCheck {

	@Override
	public String getName() {
		return "display";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		if (tag.hasKey(getName())) {
			INbtTagCompound display = tag.getCompound(getName());
			
			String name = display.getString("name");
			
			if (name != null && name.length() > protocolConstants.maxAnvilRenameChars()) {
				return false;
			}
			
		}

		return true;
	}

}
