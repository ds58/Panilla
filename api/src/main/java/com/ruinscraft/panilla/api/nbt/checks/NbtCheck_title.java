package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_title extends NbtCheck {

    public NbtCheck_title() {
        super("title", PStrictness.AVERAGE);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        final int titleLength = tag.getString("title").length();

        if (config.strictness == PStrictness.STRICT) {
            if (titleLength > protocolConstants.maxBookTitleLength()) {
                return false;
            }
        } else {
            if (titleLength > protocolConstants.NOT_PROTOCOL_maxItemNameLength()) {
                return false;
            }
        }

        return true;
    }

}
