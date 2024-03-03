package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_ench extends NbtCheck {

    // 1.12- ench
    // 1.13+ Enchantments
    public NbtCheck_ench() {
        super("ench", PStrictness.AVERAGE, "Enchantments", "StoredEnchantments");
    }

    private static EnchantmentCompat getEnchCompat(INbtTagCompound enchantment, IPanilla panilla) {
        final EnchantmentCompat enchCompat;

        if (enchantment.hasKeyOfType("id", NbtDataType.INT) || enchantment.hasKeyOfType("id", NbtDataType.SHORT)) {
            final int id = enchantment.getInt("id");
            enchCompat = EnchantmentCompat.getByLegacyId(id);
        } else if (enchantment.hasKeyOfType("id", NbtDataType.STRING)) {
            final String namedKey = enchantment.getString("id");
            enchCompat = EnchantmentCompat.getByNamedKey(namedKey);
        } else {
            panilla.getPanillaLogger().warning("Unknown enchantment: [" + String.join(", ", enchantment.getKeys()) + "]", false);
            enchCompat = null;
        }

        return enchCompat;
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        NbtCheckResult result = NbtCheckResult.PASS;
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

        if (using == null) {
            throw new IllegalStateException("Unknown enchantment tag");
        }

        INbtTagList enchantments = tag.getList(using, NbtDataType.COMPOUND);

        for (int i = 0; i < enchantments.size(); i++) {
            INbtTagCompound enchantment = enchantments.getCompound(i);
            EnchantmentCompat enchCompat = getEnchCompat(enchantment, panilla);

            if (enchCompat == null) {
                continue;
            }

            int lvl = 0xFFFF & enchantments.getCompound(i).getShort("lvl");

            if (lvl > panilla.getEnchantments().getMaxLevel(enchCompat)) {
                result = NbtCheckResult.FAIL;
                break;
            }

            if (lvl < panilla.getEnchantments().getStartLevel(enchCompat)) {
                result = NbtCheckResult.FAIL;
                break;
            }

            for (int j = 0; j < enchantments.size(); j++) {
                INbtTagCompound otherEnchantment = enchantments.getCompound(j);
                EnchantmentCompat _enchCompat = getEnchCompat(otherEnchantment, panilla);

                if (_enchCompat == null) {
                    continue;
                }

                if (enchCompat != _enchCompat) {
                    if (panilla.getEnchantments().conflicting(enchCompat, _enchCompat)) {
                        result = NbtCheckResult.FAIL;
                        break;
                    }
                }
            }
        }

        return result;
    }

}
