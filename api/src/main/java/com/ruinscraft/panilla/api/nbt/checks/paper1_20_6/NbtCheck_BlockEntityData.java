package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

import static com.ruinscraft.panilla.api.nbt.checks.paper1_20_6.NbtCheck_Container.checkItem;

public class NbtCheck_BlockEntityData extends NbtCheck {
    public NbtCheck_BlockEntityData() {
        super("minecraft:block_entity_data", PStrictness.STRICT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        tag = tag.getCompound(getName());

        if (tag.hasKey("front_text") || tag.hasKey("back_text")) return NbtCheckResult.FAIL;
        if (itemName.contains("campfire")) return NbtCheckResult.FAIL;
        if (tag.hasKey("cursors")) return NbtCheckResult.FAIL;
        if (itemName.equals("jukebox")) {
            INbtTagCompound item = tag.getCompound("RecordItem");

            FailedNbtList failedNbtList = checkItem(item, itemName, panilla);

            if (failedNbtList.containsCritical()) {
                return NbtCheckResult.CRITICAL;
            } else if (failedNbtList.findFirstNonCritical() != null) {
                return NbtCheckResult.FAIL;
            }
        }

        return NbtCheckResult.PASS;
    }
}
