package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.enchantments.Enchantment;

import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.NbtDataType;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class NbtChecker implements INbtChecker {

	private final IProtocolConstants protocolConstants;
	
	public NbtChecker(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}
	
	@Override
	public void check_Item(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			System.out.println(String.join(", ", root.c()));
		}
	}

	@Override
	public void check_ench(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("ench")) {
				NBTTagList enchantments = root.getList("ench", NbtDataType.COMPOUND.getId());

				for (int i = 0; i < enchantments.size(); i++) {
					Enchantment current = Enchantment.getById(enchantments.get(i).getShort("id"));
					int lvl = 0xFFFF & enchantments.get(i).getShort("lvl");

					if (lvl > current.getMaxLevel()) {
						throw new NbtNotPermittedException("enchantment level too high");
					}

					if (lvl < current.getStartLevel()) {
						throw new NbtNotPermittedException("enchantment level too low");
					}

					for (int j = 0; j < enchantments.size(); j++) {
						Enchantment other = Enchantment.getById(enchantments.get(j).getShort("id"));

						if (current != other && current.conflictsWith(other)) {
							throw new NbtNotPermittedException("conflicting enchantments");
						}
					}
				}
			}
		}
	}

	@Override
	public void check_display(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("display")) {
				NBTTagCompound display = root.getCompound("display");
				
				String name = display.getString("Name");

				if (name != null && name.length() > protocolConstants.maxAnvilRenameChars()) {
					throw new NbtNotPermittedException("item name too long");
				}
				
				NBTTagList lore = display.getList("Lore", NbtDataType.STRING.getId());

				if (lore != null && !lore.isEmpty()) {
					for (int i = 0; i < lore.size(); i++) {
						String line = lore.getString(i);
						
						if (line != null && line.length() > protocolConstants.NOT_PROTOCOL_maxLoreLineLength()) {
							throw new NbtNotPermittedException("lore line too long");
						}
					}
				}
			}
		}
	}

	@Override
	public void check_AttributeModifiers(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Unbreakable(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_SkullOwner(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_HideFlags(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CanDestroy(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_PickupDelay(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Age(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_generation(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CanPlaceOn(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_BlockEntityTag(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CustomPotionEffects(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_Potion(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

	@Override
	public void check_CustomPotionColor(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {

		}
	}

}
