package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_Fireworks extends NbtCheck {

	public NbtCheck_Fireworks() {
		super("Fireworks", PStrictness.AVERAGE);
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants) {
		INbtTagCompound fireworks = tag.getCompound("Fireworks");

		int flight = fireworks.getInt("Flight");

		if (flight > protocolConstants.maxFireworksFlight()
				|| flight < protocolConstants.minFireworksFlight()) {
			return false;
		}

		INbtTagList explosions = fireworks.getList("Explosions", NbtDataType.COMPOUND);

		if (explosions != null && explosions.size() > protocolConstants.maxFireworksExplosions()) {
			return false;
		}

		return true;
	}

}
