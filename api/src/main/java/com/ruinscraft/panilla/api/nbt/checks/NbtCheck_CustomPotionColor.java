package com.ruinscraft.panilla.api.nbt.checks;

import org.bukkit.Color;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionColor extends NbtCheck {

	public NbtCheck_CustomPotionColor() {
		super("CustomPotionColor", PStrictness.LENIENT);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PStrictness strictness) {
		int bgr = tag.getInt(getName());

		try {
			Color.fromBGR(bgr);
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

}
