package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class NbtCheck_CustomPotionEffects_1_20_6 extends NbtCheck {
    public NbtCheck_CustomPotionEffects_1_20_6() {
        super("minecraft:potion_contents", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        tag = tag.getCompound("minecraft:potion_contents");

        if (!tag.hasKey("custom_effects")) return NbtCheckResult.PASS;
        INbtTagList effectsList = tag.getList("custom_effects", NbtDataType.COMPOUND);

        for (int i = 0; i < effectsList.size(); i++) {
            INbtTagCompound effect = effectsList.getCompound(i);

            if (effect.hasKeyOfType("amplifier", NbtDataType.BYTE)) {
                short amplifier = effect.getByte("amplifier");
                if (amplifier > 32) {
                    return NbtCheckResult.CRITICAL;
                }
            }
        }

        return NbtCheckResult.PASS;
    }
}
