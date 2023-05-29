package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_display extends NbtCheck {

    public NbtCheck_display() {
        super("display", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        INbtTagCompound display = tag.getCompound(getName());

        String name = display.getString("Name");

        final int maxNameLength;

        // if strict, use anvil length
        if (panilla.getPanillaConfig().strictness.lvl >= PStrictness.STRICT.lvl) {
            maxNameLength = panilla.getProtocolConstants().maxAnvilRenameChars();
        } else {
            maxNameLength = panilla.getProtocolConstants().NOT_PROTOCOL_maxItemNameLength();
        }

        if (name != null && name.length() > maxNameLength) {
            return NbtCheckResult.CRITICAL;
        }

        INbtTagList lore = display.getList("Lore", NbtDataType.LIST);

        if (lore.size() > panilla.getProtocolConstants().NOT_PROTOCOL_maxLoreLines()) {
            return NbtCheckResult.CRITICAL;
        }

        return NbtCheckResult.PASS;
    }

}
