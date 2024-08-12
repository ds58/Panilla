package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class NbtCheck_PaperRange extends NbtCheck {
    public NbtCheck_PaperRange() {
        super("minecraft:custom_data", PStrictness.LENIENT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        tag = tag.getCompound(getName());

        if (tag.hasKeyOfType("Paper.Range", NbtDataType.DOUBLE)) {
            double paperRange = tag.getDouble("Paper.Range");
            if (paperRange > 2048) {
                return NbtCheckResult.CRITICAL;
            }
        }
        return NbtCheckResult.PASS;
    }
}
