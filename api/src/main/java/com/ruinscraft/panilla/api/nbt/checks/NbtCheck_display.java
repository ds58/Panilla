package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_display extends NbtCheck {

    public NbtCheck_display() {
        super("display", PStrictness.AVERAGE);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        INbtTagCompound display = tag.getCompound(getName());

        String name = display.getString("Name");

        final int maxNameLength;

        // if strict, use anvil length
        if (config.strictness.lvl >= PStrictness.STRICT.lvl) {
            maxNameLength = protocolConstants.maxAnvilRenameChars();
        }

        else {
            maxNameLength = protocolConstants.NOT_PROTOCOL_maxItemNameLength();
        }

        if (name != null && name.length() > maxNameLength) {
            return false;
        }

        INbtTagList lore = display.getList("Lore", NbtDataType.LIST);

        if (lore.size() > protocolConstants.NOT_PROTOCOL_maxLoreLines()) {
            return false;
        }

        return true;
    }

}
