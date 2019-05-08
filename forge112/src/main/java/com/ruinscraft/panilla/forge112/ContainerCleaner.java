package com.ruinscraft.panilla.forge112;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.forge112.nbt.NbtTagCompound;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerCleaner implements IContainerCleaner {

    private final IPanilla panilla;

    public ContainerCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player.getHandle();
        Container container = entityPlayerMP.inventoryContainer;

        for (int slot = 0; slot < container.inventorySlots.size(); slot++) {
            ItemStack itemStack = container.getSlot(slot).getStack();

            if (itemStack == null || !itemStack.hasTagCompound()) continue;

            NBTTagCompound tag = itemStack.getTagCompound();

            String failedNbt = NbtChecks.checkAll(
                    new NbtTagCompound(tag), itemStack.getItem().getClass().getSimpleName(), panilla);

            if (failedNbt != null) {
                tag.removeTag(failedNbt);
                container.getSlot(slot).getStack().setTagCompound(tag);
            }
        }
    }

}
