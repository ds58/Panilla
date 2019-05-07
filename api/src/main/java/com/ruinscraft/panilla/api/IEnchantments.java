package com.ruinscraft.panilla.api;

public interface IEnchantments {

    EnchantmentCompat getById(int id);

    EnchantmentCompat getByKey(String key);

    int getMaxLevel(EnchantmentCompat enchCompat);

    int getStartLevel(EnchantmentCompat enchCompat);

    boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat);

}
