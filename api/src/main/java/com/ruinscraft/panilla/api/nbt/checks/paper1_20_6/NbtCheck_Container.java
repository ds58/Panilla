package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedBlockEntityTagItemsNbt;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck_pages;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;

import java.util.HashMap;
import java.util.Map;

public class NbtCheck_Container extends NbtCheck {

    public NbtCheck_Container() {
        super("minecraft:container", PStrictness.LENIENT);
    }

    public static FailedBlockEntityTagItemsNbt checkItems(String nbtTagName, INbtTagList items, String itemName, IPanilla panilla) {
        int charCount = NbtCheck_pages.getCharCountForItems(items);

        if (charCount > 100_000) {
            return new FailedBlockEntityTagItemsNbt(nbtTagName, NbtCheck.NbtCheckResult.CRITICAL);
        }

        Map<Integer, FailedNbt> itemFails = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            FailedNbtList failedNbtList = checkItem(items.getCompound(i), itemName, panilla);

            for (FailedNbt failedNbt : failedNbtList) {
                if (FailedNbt.fails(failedNbt)) {
                    itemFails.put(i, failedNbt);
                }
            }
        }

        if (!itemFails.isEmpty()) {
            return new FailedBlockEntityTagItemsNbt(nbtTagName, NbtCheckResult.FAIL, itemFails);
        }

        return new FailedBlockEntityTagItemsNbt(nbtTagName, NbtCheckResult.PASS);
    }

    public static FailedNbtList checkItem(INbtTagCompound item, String itemName, IPanilla panilla) {
        if (item.hasKey("tag")) {
            return NbtChecks.checkAll(item.getCompound("tag"), itemName, panilla);
        } else {
            return new FailedNbtList();
        }
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagList items = tag.getList(getName(), NbtDataType.COMPOUND);

        int sizeBytes = 0;
        for (int i = 0; i < items.size(); i++) sizeBytes += items.getCompound(i).getStringSizeBytes();
        int maxSizeBytes = panilla.getProtocolConstants().NOT_PROTOCOL_maxBlockEntityTagLengthBytes();

        // ensure BlockEntityTag isn't huge
        if (sizeBytes > maxSizeBytes) {
            return NbtCheckResult.CRITICAL;
        }

        // tiles with items/containers (chests, hoppers, shulkerboxes, etc)
        // only ItemShulkerBoxes should have "Items" NBT tag in survival
        itemName = itemName.toLowerCase();

        if (panilla.getPConfig().noBlockEntityTag) {
            return NbtCheckResult.FAIL;
        }

        if (panilla.getPConfig().strictness == PStrictness.STRICT) {
            if (!(itemName.contains("shulker") || itemName.contains("itemstack") || itemName.contains("itemblock"))) {
                return NbtCheckResult.FAIL;
            }
        }

        FailedBlockEntityTagItemsNbt failedNbt = checkItems(getName(), items, itemName, panilla);

        // Only remove NBT from shulkerbox if it contains a CRITICAL item
        if (failedNbt.critical()) {
            return NbtCheckResult.CRITICAL;
        }

        return NbtCheckResult.PASS;
    }

}
