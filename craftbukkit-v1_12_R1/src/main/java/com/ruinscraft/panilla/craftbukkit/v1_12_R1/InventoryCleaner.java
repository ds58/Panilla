package com.ruinscraft.panilla.craftbukkit.v1_12_R1;

import com.ruinscraft.panilla.api.IInventoryCleaner;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_12_R1.nbt.NbtTagCompound;
import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.Slot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.List;

public class InventoryCleaner implements IInventoryCleaner {

    private static boolean is_1_12_2;

    static {
        try {
            Container.class.getField("slots");
            is_1_12_2 = true;
        } catch (NoSuchFieldException e) {
            is_1_12_2 = false;
        }
    }

    private final IPanilla panilla;

    public InventoryCleaner(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void clean(IPanillaPlayer player) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        Container container = craftPlayer.getHandle().activeContainer;

        int containerSlotsSize = 0;

        if (is_1_12_2) {
            containerSlotsSize = container.slots.size();
        } else {
            try {
                Field slotsField = Container.class.getField("c");
                List<Slot> slots = (List<Slot>) slotsField.get(container);
                containerSlotsSize = slots.size();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (int slot = 0; slot < containerSlotsSize; slot++) {
            ItemStack itemStack = container.getSlot(slot).getItem();

            if (itemStack == null || !itemStack.hasTag()) {
                continue;
            }

            NBTTagCompound nmsTag = itemStack.getTag();
            INbtTagCompound tag = new NbtTagCompound(nmsTag);
            String itemName = itemStack.getItem().getName();

            if (nmsTag == null || itemName == null) {
                continue;
            }

            FailedNbtList failedNbtList = NbtChecks.checkAll(tag, itemName, panilla);

            for (FailedNbt failedNbt : failedNbtList) {
                if (FailedNbt.failsThreshold(failedNbt)) {
                    container.getSlot(slot).getItem().setTag(null);
                    break;
                } else if (FailedNbt.fails(failedNbt)) {
                    nmsTag.remove(failedNbt.key);
                    container.getSlot(slot).getItem().setTag(nmsTag);
                    break;
                }
            }
        }
    }

}
