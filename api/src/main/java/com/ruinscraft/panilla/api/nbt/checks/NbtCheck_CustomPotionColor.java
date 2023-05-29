package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import org.bukkit.Color;

public class NbtCheck_CustomPotionColor extends NbtCheck {

    public NbtCheck_CustomPotionColor() {
        super("CustomPotionColor", PStrictness.LENIENT);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        int bgr = tag.getInt(getName());

        try {
            Color.fromBGR(bgr);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

}
