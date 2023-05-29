package com.ruinscraft.panilla.v1_14_R1;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;

public class Enchantments implements IEnchantments {

    @Override
    public EnchantmentCompat getById(int id) {
        return null;
    }

    @Override
    public EnchantmentCompat getByKey(String key) {
        return null;
    }

    @Override
    public int getMaxLevel(EnchantmentCompat enchCompat) {
        return 0;
    }

    @Override
    public int getStartLevel(EnchantmentCompat enchCompat) {
        return 0;
    }

    @Override
    public boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat) {
        return false;
    }

}
