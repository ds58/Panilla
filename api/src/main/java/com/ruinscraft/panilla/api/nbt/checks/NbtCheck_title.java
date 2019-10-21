package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_title extends NbtCheck {

    public NbtCheck_title() {
        super("title", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        final int titleLength = tag.getString("title").length();

        if (panilla.getPanillaConfig().strictness == PStrictness.STRICT) {
            if (titleLength > panilla.getProtocolConstants().maxBookTitleLength()) {
                return NbtCheckResult.CRITICAL;
            }
        } else {
            if (titleLength > panilla.getProtocolConstants().NOT_PROTOCOL_maxItemNameLength()) {
                return NbtCheckResult.CRITICAL;
            }
        }

        return NbtCheckResult.PASS;
    }

}
