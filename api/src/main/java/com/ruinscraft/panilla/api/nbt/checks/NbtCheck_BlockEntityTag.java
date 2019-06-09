package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_BlockEntityTag extends NbtCheck {

    private static final int MAX_STRING_SIZE_BYTES = 3612536 / 2;

    public NbtCheck_BlockEntityTag() {
        super("BlockEntityTag", PStrictness.LENIENT);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        INbtTagCompound blockEntityTag = tag.getCompound(getName());

        // ensure BlockEntityTag isn't huge
        if (blockEntityTag.getStringSizeBytes() > MAX_STRING_SIZE_BYTES) {
            return false;
        }

        if (panilla.getPanillaConfig().strictness == PStrictness.STRICT) {
            // locked chests
            if (blockEntityTag.hasKey("Lock")) {
                return false;
            }

            // signs with text
            if (blockEntityTag.hasKey("Text1")
                    || blockEntityTag.hasKey("Text2")
                    || blockEntityTag.hasKey("Text3")
                    || blockEntityTag.hasKey("Text4")) {
                return false;
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
                        return false;
                }
            }

            INbtTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND);

            if (!checkItems(items, nmsItemClassName, panilla)) {
                return false;
            }
        }

        return true;
    }

    // true if ok, false if not ok
    private static boolean checkItems(INbtTagList items, String nmsItemClassName, IPanilla panilla) {
        for (int i = 0; i < items.size(); i++) {
            INbtTagCompound item = items.getCompound(i);

            if (item.hasKey("tag")) {
                String failedNbt = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, panilla);

                if (failedNbt != null) return false;
            }
        }

        return true;
    }

}
