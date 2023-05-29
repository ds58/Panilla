package com.ruinscraft.panilla.api.nbt.checks;

import org.bukkit.Color;

import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionColor implements NbtCheck {

	@Override
	public String getName() {
		return "CustomPotionColor";
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName) {
		if (tag.hasKey(getName())) {
			int bgr = tag.getInt(getName());

			try {
				Color.fromBGR(bgr);
			} catch (IllegalArgumentException e) {
				return false;
			}
			
		}
		
		return true;
	}
	
}
