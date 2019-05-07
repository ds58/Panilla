package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_ench extends NbtCheck {

    public NbtCheck_ench() {
        super("ench", PStrictness.AVERAGE, "Enchantments", "StoredEnchantments");
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
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
            INbtTagCompound enchantment = enchantments.getCompound(i);
            EnchantmentCompat enchCompat = getEnchCompat(enchantment, panilla.getEnchantments());

            if (enchCompat == null) {
                continue;
            }

            int lvl = 0xFFFF & enchantments.getCompound(i).getShort("lvl");

            if (lvl > panilla.getEnchantments().getMaxLevel(enchCompat)) {
                return false;
            }

            if (lvl < panilla.getEnchantments().getStartLevel(enchCompat)) {
                return false;
            }

            for (int j = 0; j < enchantments.size(); j++) {
                INbtTagCompound otherEnchantment = enchantments.getCompound(j);
                EnchantmentCompat _enchCompat = getEnchCompat(otherEnchantment, panilla.getEnchantments());

                if (enchCompat != _enchCompat) {
                    if (panilla.getEnchantments().conflicting(enchCompat, _enchCompat)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static EnchantmentCompat getEnchCompat(INbtTagCompound enchantment, IEnchantments enchantments) {
        if (enchantment.hasKeyOfType("id", NbtDataType.INT) || enchantment.hasKeyOfType("id", NbtDataType.SHORT)) {
            final int id = enchantment.getInt("id");
            return enchantments.getById(id);
        } else if (enchantment.hasKeyOfType("id", NbtDataType.STRING)) {
            final String namedKey = enchantment.getString("id");
            return enchantments.getByKey(namedKey);
        }
        return null;
    }

}
