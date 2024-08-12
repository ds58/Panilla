package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_AttributeModifiers extends NbtCheck {

    public NbtCheck_AttributeModifiers() {
        super("AttributeModifiers", PStrictness.AVERAGE, "minecraft:attribute_modifiers");
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        return NbtCheckResult.FAIL;
    }

}
