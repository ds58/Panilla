package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedBlockEntityTagItemsNbt;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.HashMap;
import java.util.Map;

public class NbtCheck_BlockEntityTag extends NbtCheck {

    public NbtCheck_BlockEntityTag() {
        super("BlockEntityTag", PStrictness.LENIENT);
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
        NbtCheckResult result = NbtCheckResult.PASS;
        INbtTagCompound blockEntityTag = tag.getCompound(getName());

        int sizeBytes = blockEntityTag.getStringSizeBytes();
        int maxSizeBytes = panilla.getProtocolConstants().NOT_PROTOCOL_maxBlockEntityTagLengthBytes();

        // ensure BlockEntityTag isn't huge
        if (sizeBytes > maxSizeBytes) {
            return NbtCheckResult.CRITICAL;
        }

        if (blockEntityTag.hasKey("LootTable")) {
            String lootTable = blockEntityTag.getString("LootTable");

            lootTable = lootTable.trim();

            if (lootTable.isEmpty()) {
                return NbtCheckResult.CRITICAL;
            }

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
                result = NbtCheckResult.FAIL;
            }

            // signs with text
            if (blockEntityTag.hasKey("Text1")
                    || blockEntityTag.hasKey("Text2")
                    || blockEntityTag.hasKey("Text3")
                    || blockEntityTag.hasKey("Text4")) {
                result = NbtCheckResult.FAIL;
            }
        }

        // tiles with items/containers (chests, hoppers, shulkerboxes, etc)
        if (blockEntityTag.hasKey("Items")) {
            // only ItemShulkerBoxes should have "Items" NBT tag in survival
            itemName = itemName.toLowerCase();

            if (panilla.getPConfig().noBlockEntityTag) {
                result = NbtCheckResult.FAIL;
            }

            if (panilla.getPConfig().strictness == PStrictness.STRICT) {
                if (!(itemName.contains("shulker") || itemName.contains("itemstack") || itemName.contains("itemblock"))) {
                    result = NbtCheckResult.FAIL;
                }
            }

            // Campfires should not have BlockEntityTag
            if (itemName.contains("campfire")) {
                result = NbtCheckResult.FAIL;
            }

            INbtTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND);
            FailedBlockEntityTagItemsNbt failedNbt = checkItems(getName(), items, itemName, panilla);

            // Only remove NBT from shulkerbox if it contains a CRITICAL item
            if (failedNbt.critical()) {
                return NbtCheckResult.CRITICAL;
            }

//            if (FailedNbt.fails(failedNbt)) {
//                return failedNbt.result;
//            }
        }

        // /give @p furnace{BlockEntityTag:{RecipesUsed:{"minecraft:bow":1}}} 1
        // Breaking this furnace in the world will cause a client crash
        // https://github.com/ds58/Panilla/issues/120
        if (blockEntityTag.hasKey("RecipesUsed")) {
            INbtTagCompound recipesUsed = blockEntityTag.getCompound("RecipesUsed");

            if (recipesUsed.hasKeyOfType("minecraft:bow", NbtDataType.INT)) {
                return NbtCheckResult.CRITICAL;
            }
        }

        // check the item within a JukeBox
        if (blockEntityTag.hasKey("RecordItem")) {
            INbtTagCompound item = blockEntityTag.getCompound("RecordItem");

            FailedNbtList failedNbtList = checkItem(item, itemName, panilla);

            if (failedNbtList.containsCritical()) {
                return NbtCheckResult.CRITICAL;
            } else if (failedNbtList.findFirstNonCritical() != null) {
                result = NbtCheckResult.FAIL;
            }
        }

        if (blockEntityTag.hasKey("cursors")) {
            result = NbtCheckResult.FAIL;
        }

        // https://github.com/PaperMC/Paper/blob/master/patches/server/0490-Beacon-API-custom-effect-ranges.patch
        // Make sure any custom Paper beacon range NBT isn't ridiculous
        // Related server crash https://pastebin.com/tDPtS60Q
        if (blockEntityTag.hasKeyOfType("Paper.Range", NbtDataType.DOUBLE)) {
            double paperRange = blockEntityTag.getDouble("Paper.Range");
            if (paperRange > 2048) {
                return NbtCheckResult.CRITICAL;
            }
        }

        return result;
    }

}
