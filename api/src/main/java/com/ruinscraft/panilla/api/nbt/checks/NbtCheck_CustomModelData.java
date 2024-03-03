package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_CustomModelData extends NbtCheck {

    // introduced in 1.14
    public NbtCheck_CustomModelData() {
        super("CustomModelData", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        try {
            tag.getInt("CustomModelData");
        } catch (Exception e) {
            return NbtCheckResult.CRITICAL;
        }

        return NbtCheckResult.PASS;
    }

}
