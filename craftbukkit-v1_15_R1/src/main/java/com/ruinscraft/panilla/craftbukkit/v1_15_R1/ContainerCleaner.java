package com.ruinscraft.panilla.craftbukkit.v1_15_R1;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_15_R1.nbt.NbtTagCompound;
import net.minecraft.server.v1_15_R1.Container;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

public class ContainerCleaner implements IContainerCleaner {

    private final IPanilla panilla;

    public ContainerCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        Container container = craftPlayer.getHandle().activeContainer;

        for (int slot = 0; slot < container.slots.size(); slot++) {
            ItemStack itemStack = container.getSlot(slot).getItem();

            if (itemStack == null || !itemStack.hasTag()) {
                continue;
            }

            NBTTagCompound nmsTag = itemStack.getTag();
            INbtTagCompound tag = new NbtTagCompound(nmsTag);
            FailedNbt failedNbt = NbtChecks.checkAll(tag, itemStack.getClass().getSimpleName(), panilla);

            if (FailedNbt.fails(failedNbt)) {
                nmsTag.remove(failedNbt.key);
                container.getSlot(slot).getItem().setTag(nmsTag);
            }
        }
    }

}
