package com.ruinscraft.panilla.sponge;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.enchantment.EnchantmentType;

public class SpongeEnchantments implements IEnchantments {

    @Override
    public int getMaxLevel(EnchantmentCompat enchCompat) {
        return getSpongeEnch(enchCompat).getMaximumLevel();
    }

    @Override
    public int getStartLevel(EnchantmentCompat enchCompat) {
        return getSpongeEnch(enchCompat).getMinimumLevel();
    }

    @Override
    public boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat) {
        return getSpongeEnch(enchCompat).isCompatibleWith(getSpongeEnch(_enchCompat));
    }

    private static EnchantmentType getSpongeEnch(EnchantmentCompat enchCompat) {
        return Sponge.getGame().getRegistry().getType(EnchantmentType.class, enchCompat.namedKey).get();
    }

}
