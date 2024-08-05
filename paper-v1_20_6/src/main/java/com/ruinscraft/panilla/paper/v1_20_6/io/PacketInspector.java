package com.ruinscraft.panilla.paper.v1_20_6.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.*;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.paper.v1_20_6.nbt.NbtTagCompound;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PacketInspector implements IPacketInspector {
    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkPacketPlayInClickContainer(Object packetHandle) throws NbtNotPermittedException {
        if (!(packetHandle instanceof PacketPlayInWindowClick)) return;
        PacketPlayInWindowClick packet = (PacketPlayInWindowClick) packetHandle;

        int slot = packet.f();
        ItemStack item = packet.g();
        if (item == null || item.isEmpty() || item.getComponents().isEmpty()) return;

        NbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(item.getBukkitStack()).getCompound("components"));
        String itemClass = item.getDescriptionId();
        String packetClass = "PacketPlayInWindowClick";

        NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object packetHandle) throws NbtNotPermittedException {
        if (!(packetHandle instanceof PacketPlayInSetCreativeSlot)) return;
        PacketPlayInSetCreativeSlot packet = (PacketPlayInSetCreativeSlot) packetHandle;

        int slot = packet.b();
        ItemStack item = packet.e();
        if (item == null || item.isEmpty() || item.getComponents().isEmpty()) return;

        NbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(item.getBukkitStack()).getCompound("components"));
        String itemClass = item.getDescriptionId();
        String packetClass = "PacketPlayInSetCreativeSlot";

        NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object packetHandle) throws NbtNotPermittedException {
        if (!(packetHandle instanceof PacketPlayOutSetSlot)) return;
        PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) packetHandle;

        int windowId = packet.b();

        // check if window is not player inventory and we are ignoring non-player inventories
        if (windowId != 0 && panilla.getPConfig().ignoreNonPlayerInventories) {
            return;
        }

        int slot = packet.e();

        ItemStack item = packet.f();

        if (item == null || item.isEmpty() || item.getComponents().isEmpty()) {
            return;
        }

        NbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(item.getBukkitStack()).getCompound("components"));
        String itemClass = item.getClass().getSimpleName();
        String packetClass = packet.getClass().getSimpleName();

        NbtChecks.checkPacketPlayOut(slot, tag, itemClass, packetClass, panilla);
    }

    @Override
    public void checkPacketPlayOutWindowItems(Object packetHandle) throws NbtNotPermittedException {
        if (!(packetHandle instanceof PacketPlayOutWindowItems)) return;
        PacketPlayOutWindowItems packet = (PacketPlayOutWindowItems) packetHandle;

        int windowId = packet.b();

        // check if window is not player inventory
        if (windowId != 0) {
            return;
        }

        List<ItemStack> itemStacks = packet.e();

        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty() || itemStack.getComponents().isEmpty()) {
                continue;
            }

            NbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(itemStack.asBukkitCopy()).getCompound("components"));
            String itemClass = itemStack.getClass().getSimpleName();
            String packetClass = packet.getClass().getSimpleName();

            NbtChecks.checkPacketPlayOut(0, tag, itemClass, packetClass, panilla); // TODO: set slot?
        }
    }

    @Override
    public void checkPacketPlayOutSpawnEntity(Object packetHandle) throws EntityNbtNotPermittedException {
        if ((!(packetHandle instanceof PacketPlayOutSpawnEntity))) return;

        PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) packetHandle;

        UUID entityId = packet.e();
        Entity entity = null;

        for (ServerLevel worldServer : MinecraftServer.getServer().getAllLevels()) {
            entity = worldServer.getEntityLookup().get(entityId);
            if (entity != null) break;
        }

        if (!(entity instanceof EntityItem)) return;

        EntityItem item = (EntityItem) entity;
        ItemStack itemStack = item.p();

        if (itemStack == null) {
            return;
        }

        if (itemStack.isEmpty() || itemStack.getComponents().isEmpty()) {
            return;
        }

        INbtTagCompound tag = new NbtTagCompound(NBT.itemStackToNBT(itemStack.getBukkitStack()).getCompound("components"));
        String itemName = itemStack.getItem().getDescriptionId();
        String worldName = "";

        try {
            Field worldField = Entity.class.getDeclaredField("level");
            worldField.setAccessible(true);
            Level world = (Level) worldField.get(entity);
            worldName = world.getWorld().getName();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        FailedNbtList failedNbtList = NbtChecks.checkAll(tag, itemName, panilla);

        if (failedNbtList.containsCritical()) {
            throw new EntityNbtNotPermittedException(packet.getClass().getSimpleName(), false, failedNbtList.getCritical(), entityId, worldName);
        }

        FailedNbt failedNbt = failedNbtList.findFirstNonCritical();

        if (failedNbt != null) {
            throw new EntityNbtNotPermittedException(packet.getClass().getSimpleName(), false, failedNbt, entityId, worldName);
        }
    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        ServerPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(entityPlayer.containerMenu.containerId, entityPlayer.containerMenu.incrementStateId(), slot, new ItemStack(Blocks.AIR));
        entityPlayer.connection.send(packet);
    }

    @Override
    public void stripNbtFromItemEntity(UUID entityId) {
        Entity entity = null;

        for (ServerLevel worldServer : MinecraftServer.getServer().getAllLevels()) {
            entity = worldServer.getEntityLookup().get(entityId);
            if (entity != null) break;
        }

        if (entity instanceof EntityItem) {
            EntityItem item = (EntityItem) entity;
            ItemStack itemStack = item.p();
            if (itemStack == null || itemStack.isEmpty() || itemStack.getComponents().isEmpty()) return;
            Iterator<TypedDataComponent<?>> iter = itemStack.getComponents().iterator();
            while (iter.hasNext()) iter.remove();
        }
    }

    @Override
    public void stripNbtFromItemEntityLegacy(int entityId) {
        throw new RuntimeException("cannot use #stripNbtFromItemEntityLegacy on 1.20.6");
    }

    @Override
    public void validateBaseComponentParse(String string) throws Exception {
        CraftChatMessage.fromJSON(string);
    }
}
