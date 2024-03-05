package com.ruinscraft.panilla.craftbukkit.v1_16_R3.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_16_R3.nbt.NbtTagCompound;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class PacketInspector implements IPacketInspector {

    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkPacketPlayInClickContainer(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayInWindowClick) {
            PacketPlayInWindowClick packet = (PacketPlayInWindowClick) _packet;

            int slot = packet.c();
            ItemStack itemStack = packet.f();

            if (itemStack == null || !itemStack.hasTag()) {
                return;
            }

            NbtTagCompound tag = new NbtTagCompound(itemStack.getTag());
            String itemClass = itemStack.getItem().getClass().getSimpleName();
            String packetClass = "PacketPlayInWindowClick";

            NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
        }
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot packet = (PacketPlayInSetCreativeSlot) _packet;

            int slot = packet.b();
            ItemStack itemStack = packet.getItemStack();

            if (itemStack == null || !itemStack.hasTag()) return;

            NbtTagCompound tag = new NbtTagCompound(itemStack.getTag());
            String itemClass = itemStack.getItem().getClass().getSimpleName();
            String packetClass = packet.getClass().getSimpleName();

            NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSetSlot) {
            PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) _packet;

            try {
                Field windowIdField = PacketPlayOutSetSlot.class.getDeclaredField("a");

                windowIdField.setAccessible(true);

                int windowId = (int) windowIdField.get(packet);

                // check if window is not player inventory and we are ignoring non-player inventories
                if (windowId != 0 && panilla.getPConfig().ignoreNonPlayerInventories) {
                    return;
                }

                Field slotField = PacketPlayOutSetSlot.class.getDeclaredField("b");
                Field itemStackField = PacketPlayOutSetSlot.class.getDeclaredField("c");

                slotField.setAccessible(true);
                itemStackField.setAccessible(true);

                int slot = (int) slotField.get(packet);
                ItemStack itemStack = (ItemStack) itemStackField.get(packet);

                if (itemStack == null || !itemStack.hasTag()) return;

                NbtTagCompound tag = new NbtTagCompound(itemStack.getTag());
                String itemClass = itemStack.getClass().getSimpleName();
                String packetClass = packet.getClass().getSimpleName();

                NbtChecks.checkPacketPlayOut(slot, tag, itemClass, packetClass, panilla);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void checkPacketPlayOutWindowItems(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutWindowItems) {
            PacketPlayOutWindowItems packet = (PacketPlayOutWindowItems) _packet;

            try {
                Field windowIdField = PacketPlayOutWindowItems.class.getDeclaredField("a");

                windowIdField.setAccessible(true);

                int windowId = (int) windowIdField.get(packet);

                // check if window is not player inventory
                if (windowId != 0) {
                    return;
                }

                Field itemStacksField = PacketPlayOutWindowItems.class.getDeclaredField("b");

                itemStacksField.setAccessible(true);

                List<ItemStack> itemStacks = (List<ItemStack>) itemStacksField.get(packet);

                for (ItemStack itemStack : itemStacks) {
                    if (itemStack == null || !itemStack.hasTag()) {
                        continue;
                    }

                    NbtTagCompound tag = new NbtTagCompound(itemStack.getTag());
                    String itemClass = itemStack.getClass().getSimpleName();
                    String packetClass = packet.getClass().getSimpleName();

                    NbtChecks.checkPacketPlayOut(0, tag, itemClass, packetClass, panilla); // TODO: set slot?
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void checkPacketPlayOutSpawnEntity(Object _packet) throws EntityNbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSpawnEntity) {
            PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) _packet;

            try {
                Field typeField = PacketPlayOutSpawnEntity.class.getDeclaredField("b");

                typeField.setAccessible(true);

                UUID entityId = (UUID) typeField.get(packet);
                org.bukkit.entity.Entity bukkitEntity = org.bukkit.Bukkit.getEntity(entityId);
                CraftEntity craftEntity = (CraftEntity) bukkitEntity;

                if (craftEntity == null) {
                    return;
                }

                Entity entity = craftEntity.getHandle();

                if (entity != null) {
                    if (entity instanceof EntityItem) {
                        EntityItem item = (EntityItem) entity;

                        if (item.getItemStack() == null) {
                            return;
                        }

                        if (!item.getItemStack().hasTag()) {
                            return;
                        }

                        INbtTagCompound tag = new NbtTagCompound(item.getItemStack().getTag());
                        String itemName = item.getItemStack().getItem().getName();
                        FailedNbtList failedNbtList = NbtChecks.checkAll(tag, itemName, panilla);

                        if (failedNbtList.containsCritical()) {
                            throw new EntityNbtNotPermittedException(packet.getClass().getSimpleName(), false, failedNbtList.getCritical(), entityId, bukkitEntity.getWorld().getName());
                        }

                        FailedNbt failedNbt = failedNbtList.findFirstNonCritical();

                        if (failedNbt != null) {
                            throw new EntityNbtNotPermittedException(packet.getClass().getSimpleName(), false, failedNbt, entityId, bukkitEntity.getWorld().getName());
                        }
                    }
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, slot, new ItemStack(Blocks.AIR));
        entityPlayer.playerConnection.sendPacket(packet);
    }

    @Override
    public void stripNbtFromItemEntity(UUID entityId) {
        org.bukkit.entity.Entity bukkitEntity = Bukkit.getServer().getEntity(entityId);

        if (bukkitEntity instanceof CraftEntity) {
            CraftEntity craftEntity = (CraftEntity) bukkitEntity;
            Entity entity = craftEntity.getHandle();

            if (entity instanceof EntityItem) {
                EntityItem item = (EntityItem) entity;

                if (item.getItemStack() == null) {
                    return;
                }

                if (!item.getItemStack().hasTag()) {
                    return;
                }

                item.getItemStack().setTag(null);
            }
        }
    }

    @Override
    public void stripNbtFromItemEntityLegacy(int entityId) {
        throw new RuntimeException("cannot use #stripNbtFromItemEntityLegacy on 1.16");
    }

    @Override
    public void validateBaseComponentParse(String string) throws Exception {
        IChatBaseComponent.ChatSerializer.a(string);
    }

}
