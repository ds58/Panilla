package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class NbtCheck_ContainerLoot extends NbtCheck {
    public NbtCheck_ContainerLoot() {
        super("minecraft:container_loot", PStrictness.STRICT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        tag = tag.getCompound(getName());

        String lootTable = tag.getString("loot_table");

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

        return NbtCheckResult.PASS;
    }
}
