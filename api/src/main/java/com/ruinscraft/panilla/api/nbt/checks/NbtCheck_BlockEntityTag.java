package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_BlockEntityTag extends NbtCheck {

    private static final int MAX_STRING_SIZE_BYTES = 707958;

    public NbtCheck_BlockEntityTag() {
        super("BlockEntityTag", PStrictness.LENIENT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        INbtTagCompound blockEntityTag = tag.getCompound(getName());

        // ensure BlockEntityTag isn't huge
        if (blockEntityTag.getStringSizeBytes() > MAX_STRING_SIZE_BYTES) {
            return NbtCheckResult.CRITICAL;
        }

        if (panilla.getPanillaConfig().strictness == PStrictness.STRICT) {
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
            if (panilla.getPanillaConfig().strictness == PStrictness.STRICT) {
                switch (nmsItemClassName) {
                    case "ItemShulkerBox":
                        break;
                    default:
                        return NbtCheckResult.FAIL;
                }
            }

            INbtTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND);

            FailedNbt failedNbt = checkItems(items, nmsItemClassName, panilla);

            if (failedNbt != null && failedNbt.result != NbtCheckResult.PASS) {
                return failedNbt.result;
            }
        }

        // check the item within a JukeBox
        if (blockEntityTag.hasKey("RecordItem")) {
            INbtTagCompound item = blockEntityTag.getCompound("RecordItem");

            FailedNbt failedNbt = checkItem(item, nmsItemClassName, panilla);

            if (failedNbt != null && failedNbt.result != NbtCheckResult.PASS) {
                return failedNbt.result;
            }
        }

        return null;
    }

    private static FailedNbt checkItems(INbtTagList items, String nmsItemClassName, IPanilla panilla) {
        for (int i = 0; i < items.size(); i++) {
            FailedNbt failedNbt = checkItem(items.getCompound(i), nmsItemClassName, panilla);

            if (failedNbt != null) {
                return failedNbt;
            }
        }

        return null;
    }

    private static FailedNbt checkItem(INbtTagCompound item, String nmsItemClassName, IPanilla panilla) {
        if (item.hasKey("tag")) {
            FailedNbt failedNbt = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, panilla);

            if (failedNbt != null) {
                return failedNbt;
            }
        }

        return null;
    }

}
