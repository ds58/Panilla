package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_weBrushJson extends NbtCheck {

    public NbtCheck_weBrushJson() {
        super("weBrushJson", PStrictness.LENIENT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        if (panilla.getPConfig().preventFaweBrushNbt) {
            return NbtCheckResult.FAIL;
        } else {
            return NbtCheckResult.PASS;
        }
    }

}
