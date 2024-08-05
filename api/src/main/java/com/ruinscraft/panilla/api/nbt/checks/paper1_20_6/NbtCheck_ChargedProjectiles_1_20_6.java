package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class NbtCheck_ChargedProjectiles_1_20_6 extends NbtCheck {

    public NbtCheck_ChargedProjectiles_1_20_6() {
        super("minecraft:charged_projectiles", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        NbtCheckResult result = NbtCheckResult.PASS;
        INbtTagList chargedProjectiles = tag.getList(getName(), NbtDataType.COMPOUND);

        for (int i = 0; i < chargedProjectiles.size(); i++) {
            INbtTagCompound chargedProjectile = chargedProjectiles.getCompound(i);

            if (chargedProjectile.hasKey("components")) {
                INbtTagCompound chargedProjectileTag = chargedProjectile.getCompound("components");

                if (chargedProjectileTag.hasKey("potion_contents")) {
                    String potion = chargedProjectileTag.getCompound("potion_contents").getString("potion");

                    if (potion.endsWith("empty")) {
                        result = NbtCheckResult.FAIL;
                        break;
                    }
                }
            }
        }

        return result;
    }

}
