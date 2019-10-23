package com.ruinscraft.panilla.glowstone.r2018_9_0;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.glowstone.r2018_9_0.nbt.GlowNbtHelper;
import com.ruinscraft.panilla.glowstone.r2018_9_0.nbt.NbtTagCompound;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowInventory;
import net.glowstone.inventory.GlowMetaItem;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

public class ContainerCleaner implements IContainerCleaner {

    private IPanilla panilla;

    public ContainerCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        GlowPlayer glowPlayer = (GlowPlayer) player.getHandle();
        GlowInventory glowInventory = glowPlayer.getInventory();

        for (int i = 0; i < glowInventory.getSize(); i++) {
            ItemStack itemStack = glowInventory.getItem(i);

            if (itemStack == null || !(itemStack.getItemMeta() instanceof GlowMetaItem)) {
                continue;
            }

            itemStack = itemStack.clone();  // TODO: I think this should be cloned...

            GlowMetaItem meta = (GlowMetaItem) glowInventory.getItem(i).getItemMeta();
            CompoundTag ngTag = GlowNbtHelper.getNbt(meta);
            INbtTagCompound tag = new NbtTagCompound(ngTag);
            FailedNbt failedNbt = NbtChecks.checkAll(tag, itemStack.getClass().getSimpleName(), panilla);

            if (FailedNbt.fails(failedNbt)) {
                ngTag.remove(failedNbt.key);
                GlowNbtHelper.applyNbt(meta, ngTag);
                itemStack.setItemMeta(meta);
                glowInventory.setItem(i, itemStack);
            }
        }
    }

}
