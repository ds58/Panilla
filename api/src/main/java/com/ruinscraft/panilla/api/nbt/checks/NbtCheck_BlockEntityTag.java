package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_BlockEntityTag extends NbtCheck {

    public NbtCheck_BlockEntityTag() {
        super("BlockEntityTag", PStrictness.LENIENT);
    }

    private FailedNbt checkItems(INbtTagList items, String itemName, IPanilla panilla) {
        int charCount = NbtCheck_pages.getCharCountForItems(items);

        if (charCount > 100_000) {
            return new FailedNbt(getName(), NbtCheck.NbtCheckResult.CRITICAL);
        }

        for (int i = 0; i < items.size(); i++) {
            FailedNbt failedNbt = checkItem(items.getCompound(i), itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt;
            }
        }

        return FailedNbt.NO_FAIL;
    }

    private FailedNbt checkItem(INbtTagCompound item, String itemName, IPanilla panilla) {
        if (item.hasKey("tag")) {
            FailedNbt failedNbt = NbtChecks.checkAll(item.getCompound("tag"), itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt;
            }
        }

        return FailedNbt.NO_FAIL;
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagCompound blockEntityTag = tag.getCompound(getName());

        int sizeBytes = blockEntityTag.getStringSizeBytes();
        int maxSizeBytes = panilla.getProtocolConstants().NOT_PROTOCOL_maxBlockEntityTagLengthBytes();

        // ensure BlockEntityTag isn't huge
        if (sizeBytes > maxSizeBytes) {
            return NbtCheckResult.CRITICAL;
        }

        if (blockEntityTag.hasKey("LootTable")) {
            String lootTable = blockEntityTag.getString("LootTable");

            if (lootTable.contains(":")) {
                String[] keySplit = lootTable.split(":");

                if (keySplit.length < 2) {
                    return NbtCheckResult.CRITICAL;
                }

                String namespace = keySplit[0];
                String key = keySplit[1];

                if (namespace.isEmpty() || key.isEmpty()) {
                    return NbtCheckResult.CRITICAL;
                }
            }
        }

        if (panilla.getPConfig().strictness == PStrictness.STRICT) {
            // locked chests
            if (blockEntityTag.hasKey("Lock")) {
                return NbtCheckResult.FAIL;
            }

            // signs with text
            if (blockEntityTag.hasKey("Text1")
                    || blockEntityTag.hasKey("Text2")
                    || blockEntityTag.hasKey("Text3")
                    || blockEntityTag.hasKey("Text4")) {
                return NbtCheckResult.FAIL;
            }
        }

        // tiles with items/containers (chests, hoppers, shulkerboxes, etc)
        if (blockEntityTag.hasKey("Items")) {
            // only ItemShulkerBoxes should have "Items" NBT tag in survival
            itemName = itemName.toLowerCase();

            if (!(itemName.contains("shulker") || itemName.contains("itemstack") || itemName.contains("itemblock"))) {
                return NbtCheckResult.FAIL;
            }

            // Campfires should not have BlockEntityTag
            if (itemName.contains("campfire")) {
                return NbtCheckResult.FAIL;
            }

            INbtTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND);
            FailedNbt failedNbt = checkItems(items, itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt.result;
            }
        }

        // check the item within a JukeBox
        if (blockEntityTag.hasKey("RecordItem")) {
            INbtTagCompound item = blockEntityTag.getCompound("RecordItem");

            FailedNbt failedNbt = checkItem(item, itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt.result;
            }
        }

        return NbtCheckResult.PASS;
    }

}
