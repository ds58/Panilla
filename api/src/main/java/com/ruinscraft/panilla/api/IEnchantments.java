package com.ruinscraft.panilla.api;

public interface IEnchantments {

    int getMaxLevel(EnchantmentCompat enchCompat);

    int getStartLevel(EnchantmentCompat enchCompat);

    boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat);

}
