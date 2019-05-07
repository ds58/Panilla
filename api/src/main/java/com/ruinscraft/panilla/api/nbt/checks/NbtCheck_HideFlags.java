package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_HideFlags extends NbtCheck {

    public NbtCheck_HideFlags() {
        super("HideFlags", PStrictness.AVERAGE);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        return false;
    }

}
