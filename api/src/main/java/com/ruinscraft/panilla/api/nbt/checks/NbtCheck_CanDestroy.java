package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CanDestroy extends NbtCheck {

    public NbtCheck_CanDestroy() {
        super("CanDestroy", PStrictness.STRICT, "minecraft:can_break");
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        return NbtCheckResult.FAIL;
    }

}
