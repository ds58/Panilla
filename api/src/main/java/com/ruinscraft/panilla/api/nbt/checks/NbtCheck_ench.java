package com.ruinscraft.panilla.api.nbt.checks;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_ench extends NbtCheck {

	public NbtCheck_ench() {
		super("ench", PStrictness.AVERAGE, "Enchantments", "StoredEnchantments");
	}

	@Override
	public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
		String using = null;

		if (tag.hasKey(getName())) {
			using = getName();
		} else {
			for (String alias : getAliases()) {
				if (tag.hasKey(alias)) {
					using = alias;
				}
			}
		}

		INbtTagList enchantments = tag.getList(using, NbtDataType.COMPOUND);

		for (int i = 0; i < enchantments.size(); i++) {
			INbtTagCompound enchantment = enchantments.get(i);
			Enchantment bukkitEnchantment = getEnchantment(enchantment);

			if (bukkitEnchantment == null) return true;
			
			int lvl = 0xFFFF & enchantments.get(i).getShort("lvl");

			if (lvl > bukkitEnchantment.getMaxLevel()) {
				return false;
			}

			if (lvl < bukkitEnchantment.getStartLevel()) {
				return false;
			}
			
			for (int j = 0; j < enchantments.size(); j++) {
				INbtTagCompound otherEnchantment = enchantments.get(j);
				Enchantment otherBukkitEnchantment = getEnchantment(otherEnchantment);
				
				if (bukkitEnchantment != otherBukkitEnchantment
						&& bukkitEnchantment.conflictsWith(otherBukkitEnchantment)) {
					return false;
				}
			}
		}

		return true;
	}

	private static Enchantment getEnchantment(INbtTagCompound enchantment) {
		if (enchantment.hasKeyOfType("id", NbtDataType.INT)) {
			final int id = enchantment.getInt("id");

			try {
				// 1.12
				return (Enchantment) Enchantment.class.getMethod("getById", int.class).invoke(null, id);
			} catch (Exception e1) {
				// 1.13
				try {
					return (Enchantment) Enchantment.class.getMethod("getByName", String.class)
							.invoke(null, EnchantmentCompat.getById(id).legacyName);
				} catch (Exception e2) {
					return null;
				}
			}
		}

		else if (enchantment.hasKeyOfType("id", NbtDataType.STRING)) {
			final String namedKey = enchantment.getString("id");

			try {
				// 1.13
				return (Enchantment) Enchantment.class.getMethod("getByKey", NamespacedKey.class)
						.invoke(null, NamespacedKey.minecraft(namedKey));
			} catch (Exception e1) {
				// 1.12
				try {
					return (Enchantment) Enchantment.class.getMethod("getByName", String.class)
							.invoke(null, EnchantmentCompat.getByNamedKey(namedKey).legacyName);
				} catch (Exception e2) {
					return null;
				}
			}
		}

		return null;
	}

}
