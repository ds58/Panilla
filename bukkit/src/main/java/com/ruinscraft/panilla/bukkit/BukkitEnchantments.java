package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;
import org.bukkit.enchantments.Enchantment;

public class BukkitEnchantments implements IEnchantments {

    @Override
    public int getMaxLevel(EnchantmentCompat enchCompat) {
        return Enchantment.getByName(enchCompat.namedKey).getMaxLevel();
    }

    @Override
    public int getStartLevel(EnchantmentCompat enchCompat) {
        return Enchantment.getByName(enchCompat.namedKey).getStartLevel();
    }

    @Override
    public boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat) {
        return Enchantment.getByName(enchCompat.namedKey)
                .conflictsWith(Enchantment.getByName(_enchCompat.namedKey));
    }

}
