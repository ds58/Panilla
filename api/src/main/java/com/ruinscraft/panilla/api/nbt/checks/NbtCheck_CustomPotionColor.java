package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomPotionColor extends NbtCheck {

    public NbtCheck_CustomPotionColor() {
        super("CustomPotionColor", PStrictness.LENIENT);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        int bgr = tag.getInt(getName());
        return panilla.getPlatform().isValidPotionColor(bgr);
    }

}
