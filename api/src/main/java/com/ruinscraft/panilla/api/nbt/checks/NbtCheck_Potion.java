package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_Potion extends NbtCheck {

    public NbtCheck_Potion() {
        super("Potion", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        return NbtCheckResult.PASS;
    }

}
