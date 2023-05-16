package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_SkullProfile extends NbtCheck {

    // See: CraftMetaSkull, GameProfile
    public NbtCheck_SkullProfile() {
        super("SkullProfile", PStrictness.LENIENT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagCompound skullProfile = tag.getCompound("SkullProfile");

        // Avoid "Name and ID cannot both be blank" in GameProfile constructor
        if (!(skullProfile.hasKey("Name") && skullProfile.hasKey("Id"))) {
            return NbtCheckResult.CRITICAL;
        }

        return NbtCheckResult.PASS;
    }

}
