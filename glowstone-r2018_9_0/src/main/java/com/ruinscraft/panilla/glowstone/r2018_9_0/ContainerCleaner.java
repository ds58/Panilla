package com.ruinscraft.panilla.glowstone.r2018_9_0;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.glowstone.r2018_9_0.nbt.NbtTagCompound;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowInventory;
import net.glowstone.inventory.GlowMetaItem;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContainerCleaner implements IContainerCleaner {

    private static boolean canAccessNbt;
    private static Method writeNbtMethod;   // Reads NBT values from a GlowMetaItem into a CompoundTag
    // See https://github.com/GlowstoneMC/Glowstone/blob/a12eebbf8aad46f4d46ff4af86c59a55a39ad49e/src/main/java/net/glowstone/inventory/GlowMetaItem.java#L178

    private static Method readNbtMethod;    // writes NBT values to a GlowMetaItem from a CompoundTag
    // See https://github.com/GlowstoneMC/Glowstone/blob/a12eebbf8aad46f4d46ff4af86c59a55a39ad49e/src/main/java/net/glowstone/inventory/GlowMetaItem.java#L155

    private IPanilla panilla;

    static {
        try {
            writeNbtMethod = GlowMetaItem.class.getMethod("writeNbt", CompoundTag.class);
            readNbtMethod = GlowMetaItem.class.getMethod("readNbt", CompoundTag.class);
            canAccessNbt = true;
        } catch (NoSuchMethodException e) {
            canAccessNbt = false;
        }
    }

    public ContainerCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    private static CompoundTag getNbt(GlowMetaItem glowMetaItem) {
        CompoundTag tag = new CompoundTag();

        if (canAccessNbt) {
            try {
                writeNbtMethod.invoke(glowMetaItem, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return tag;
    }

    private static void applyNbt(GlowMetaItem glowMetaItem, CompoundTag tag) {
        if (canAccessNbt) {
            try {
                readNbtMethod.invoke(glowMetaItem, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
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

            GlowMetaItem meta = (GlowMetaItem) glowInventory.getItem(i).getItemMeta();
            CompoundTag ngTag = getNbt(meta);
            INbtTagCompound tag = new NbtTagCompound(ngTag);
            FailedNbt failedNbt = NbtChecks.checkAll(tag, itemStack.getClass().getSimpleName(), panilla);

            if (failedNbt != null && failedNbt.result != NbtCheck.NbtCheckResult.PASS) {
                ngTag.remove(failedNbt.key);
                applyNbt(meta, ngTag);
                itemStack.setItemMeta(meta);
                glowInventory.setItem(i, itemStack);
            }
        }
    }

}
