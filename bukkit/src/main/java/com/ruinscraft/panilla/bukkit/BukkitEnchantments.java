package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;
import org.bukkit.enchantments.Enchantment;

public class BukkitEnchantments implements IEnchantments {

    @Override
    public int getMaxLevel(EnchantmentCompat enchCompat) {
        Enchantment bukkitEnchantment = Enchantment.getByName(enchCompat.namedKey);
        if (bukkitEnchantment == null) {
            return Integer.MAX_VALUE; // unknown enchantment
        }
        return bukkitEnchantment.getMaxLevel();
    }

    @Override
    public int getStartLevel(EnchantmentCompat enchCompat) {
        Enchantment bukkitEnchantment = Enchantment.getByName(enchCompat.namedKey);
        if (bukkitEnchantment == null) {
            return Integer.MAX_VALUE; // unknown enchantment
        }
        return bukkitEnchantment.getStartLevel();
    }

    @Override
    public boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat) {
        Enchantment bukkitEnchantment = Enchantment.getByName(enchCompat.namedKey);
        Enchantment _bukkitEnchantment = Enchantment.getByName(_enchCompat.namedKey);
        if (bukkitEnchantment == null || _bukkitEnchantment == null) {
            return false; // unknown enchantment
        }
        return bukkitEnchantment.conflictsWith(_bukkitEnchantment);
    }

}
