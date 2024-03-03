package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionEffects extends NbtCheck {

    public NbtCheck_CustomPotionEffects() {
        super("CustomPotionEffects", PStrictness.AVERAGE, "custom_potion_effects");
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        return NbtCheckResult.FAIL;
    }

}
