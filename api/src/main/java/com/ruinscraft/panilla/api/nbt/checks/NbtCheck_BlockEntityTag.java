package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_BlockEntityTag extends NbtCheck {

    public NbtCheck_BlockEntityTag() {
        super("BlockEntityTag", PStrictness.LENIENT);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        INbtTagCompound blockEntityTag = tag.getCompound(getName());

        if (config.strictness == PStrictness.STRICT) {
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
            if (config.strictness == PStrictness.STRICT) {
                switch (nmsItemClassName) {
                    case "ItemShulkerBox":
                        break;
                    default:
                        return false;
                }
            }

            INbtTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND);

            if (!checkItems(items, nmsItemClassName, protocolConstants, config)) {
                return false;
            }
        }

        return true;
    }

    // true if ok, false if not ok
    private static boolean checkItems(INbtTagList items, String nmsItemClassName,
                                      IProtocolConstants protocolConstants, PConfig config) {
        for (int i = 0; i < items.size(); i++) {
            INbtTagCompound item = items.get(i);

            if (item.hasKey("tag")) {
                String failedNbt = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, protocolConstants, config);

                if (failedNbt != null) return false;
            }
        }

        return true;
    }

}
