package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class NbtCheck_Fireworks extends NbtCheck {

    public NbtCheck_Fireworks() {
        super("minecraft:fireworks", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        NbtCheckResult result = NbtCheckResult.PASS;
        INbtTagCompound fireworks = tag.getCompound("minecraft:fireworks");

        int flight = fireworks.getInt("flight_duration");

        if (flight > panilla.getProtocolConstants().maxFireworksFlight()
            || flight < panilla.getProtocolConstants().minFireworksFlight()) {
            result = NbtCheckResult.FAIL;
        }

        INbtTagList explosions = fireworks.getList("explosions", NbtDataType.COMPOUND);

        if (explosions != null
            && explosions.size() > panilla.getProtocolConstants().maxFireworksExplosions()) {
            result = NbtCheckResult.FAIL;
        }

        return result;
    }

}
