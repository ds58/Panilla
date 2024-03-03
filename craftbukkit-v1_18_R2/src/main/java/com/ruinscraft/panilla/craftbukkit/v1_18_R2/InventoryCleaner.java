package com.ruinscraft.panilla.craftbukkit.v1_18_R2;

import com.ruinscraft.panilla.api.IInventoryCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_18_R2.nbt.NbtTagCompound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;

public class InventoryCleaner implements IInventoryCleaner {

    private final IPanilla panilla;

    public InventoryCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        Container container = craftPlayer.getHandle().bV;

        for (int slot = 0; slot < container.i.size(); slot++) {
            ItemStack itemStack = container.b(slot).e();

            if (itemStack == null || !itemStack.s()) {
                continue;
            }

            NBTTagCompound nmsTag = itemStack.t();
            INbtTagCompound tag = new NbtTagCompound(nmsTag);
            String itemName = itemStack.c().a();

            if (nmsTag == null || itemName == null) {
                continue;
            }

            FailedNbtList failedNbtList = NbtChecks.checkAll(tag, itemName, panilla);

            for (FailedNbt failedNbt : failedNbtList) {
                if (FailedNbt.failsThreshold(failedNbt)) {
                    container.b(slot).e().c((NBTTagCompound) null);
                    break;
                } else if (FailedNbt.fails(failedNbt)) {
                    nmsTag.r(failedNbt.key);
                    container.b(slot).e().c(nmsTag);
                    break;
                }
            }
        }
    }

}
