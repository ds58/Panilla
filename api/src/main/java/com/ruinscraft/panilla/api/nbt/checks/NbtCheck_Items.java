package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;

// Similar to BlockEntityTag
// Used mainly for Bundles (1.17)
public class NbtCheck_Items extends NbtCheck {

    public NbtCheck_Items() {
        super("Items", PStrictness.LENIENT);
    }

    @Override
    public NbtCheck.NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagList itemsTagList = tag.getList(getName());

        FailedNbt failedNbt = NbtCheck_BlockEntityTag.checkItems(getName(), itemsTagList, itemName, panilla);

        if (FailedNbt.fails(failedNbt)) {
            return failedNbt.result;
        } else {
            return NbtCheck.NbtCheckResult.PASS;
        }
    }

}
