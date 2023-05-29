package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.enchantments.Enchantment;

import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.NbtDataType;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class NbtChecker implements INbtChecker {

	@Override
	public void check_Item(Object object) {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			System.out.println(String.join(", ", root.c()));
		}
	}

	@Override
	public void check_ench(Object object) {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("ench")) {
				NBTTagList enchantments = root.getList("ench", NbtDataType.COMPOUND.getId());

				for (int i = 0; i < enchantments.size(); i++) {
					NBTTagCompound enchantment = enchantments.get(i);
					int id = 0xFFFF & enchantment.getShort("id");
					int lvl = 0xFFFF & enchantment.getShort("lvl");
					
					Enchantment current = Enchantment.getById(id);

					if (lvl > current.getMaxLevel()) {
						
					}
					
					if (lvl < current.getStartLevel()) {
						
					}
					
					for (int j = 0; j < enchantments.size(); j++) {
						Enchantment other = Enchantment.getById(
								enchantments.
								get(i).
								getShort("id"));
						
						if (current.conflictsWith(other)) {
							
						}
					}
				}
			}
		}
	}

	@Override
	public void check_display(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_AttributeModifiers(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Unbreakable(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_SkullOwner(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_HideFlags(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CanDestroy(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_PickupDelay(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Age(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_generation(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CanPlaceOn(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_BlockEntityTag(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CustomPotionEffects(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Potion(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CustomPotionColor(Object object) {
		if (object instanceof NBTTagCompound) {

		}
	}

}
