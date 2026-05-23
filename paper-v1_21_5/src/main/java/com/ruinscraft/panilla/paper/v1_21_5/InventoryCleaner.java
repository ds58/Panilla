package com.ruinscraft.panilla.paper.v1_21_5;

import com.ruinscraft.panilla.api.IInventoryCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.paper.v1_21_5.nbt.NbtTagCompound;
import de.tr7zw.changeme.nbtapi.NBT;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import java.util.Iterator;

public class InventoryCleaner implements IInventoryCleaner {

    private final IPanilla panilla;

    public InventoryCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        Inventory container = craftPlayer.getHandle().getInventory();

        for (int slot = 0; slot < container.getContents().size(); slot++) {
            ItemStack itemStack = container.getContents().get(slot);

            if (itemStack == null || itemStack.isEmpty() || itemStack.getComponents().isEmpty()) {
                continue;
            }

            INbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(itemStack.getBukkitStack()).getCompound("components"));
            String itemName = itemStack.getItem().getDescriptionId();

            FailedNbtList failedNbtList = NbtChecks.checkAll(tag, itemName, panilla);

            for (FailedNbt failedNbt : failedNbtList) {
                if (FailedNbt.failsThreshold(failedNbt)) {
                    Iterator<TypedDataComponent<?>> iter = itemStack.getComponents().iterator();
                    while (iter.hasNext()) iter.remove();

                    break;
                } else if (FailedNbt.fails(failedNbt)) {
                    NBT.modifyComponents(itemStack.getBukkitStack(), s -> {
                        s.removeKey(failedNbt.key);
                    });
                    break;
                }
            }
        }
    }

}
