package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionColor extends NbtCheck {

    public NbtCheck_CustomPotionColor() {
        super("CustomPotionColor", PStrictness.LENIENT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        int bgr = tag.getInt(getName());
        boolean validColor = true;  // TODO:
        if (!validColor) {
            return NbtCheckResult.CRITICAL;
        }
        return NbtCheckResult.PASS;
    }

}
