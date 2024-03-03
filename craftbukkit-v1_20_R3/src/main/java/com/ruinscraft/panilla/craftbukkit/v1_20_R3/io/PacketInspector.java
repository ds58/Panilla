package com.ruinscraft.panilla.craftbukkit.v1_20_R3.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_20_R3.nbt.NbtTagCompound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class PacketInspector implements IPacketInspector {

    private static boolean paperChunkSystem = false;
    private static MethodHandle getEntityLookupMethodHandle = null;
    private static MethodHandle getEntityMethodHandle = null;

    static {
        try {
            Class<?> entityLookupClass = Class.forName("io.papermc.paper.chunk.system.entity.EntityLookup");
            getEntityLookupMethodHandle = MethodHandles.lookup().findVirtual(WorldServer.class, "getEntityLookup", MethodType.methodType(entityLookupClass));
            getEntityMethodHandle = MethodHandles.lookup().findVirtual(entityLookupClass, "get", MethodType.methodType(Entity.class, UUID.class));
            paperChunkSystem = true;
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Entity getChunkSystemEntity(WorldServer worldServer, UUID entityId) {
        try {
            Object entityLookup = getEntityLookupMethodHandle.invoke(worldServer);
            return (Entity) getEntityMethodHandle.invoke(entityLookup, entityId);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkPacketPlayInClickContainer(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayInWindowClick) {
            PacketPlayInWindowClick packet = (PacketPlayInWindowClick) _packet;

            int slot = packet.d();
            ItemStack itemStack = packet.f();

            if (itemStack == null || !itemStack.u()) {
                return;
            }

            NbtTagCompound tag = new NbtTagCompound(itemStack.w());
            String itemClass = itemStack.q();
            String packetClass = "PacketPlayInWindowClick";

            NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
        }
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot packet = (PacketPlayInSetCreativeSlot) _packet;

            int slot = packet.a();
            ItemStack itemStack = packet.d();

            if (itemStack == null || !itemStack.u()) {
                return;
            }

            NbtTagCompound tag = new NbtTagCompound(itemStack.w());
            String itemClass = itemStack.q();
            String packetClass = "PacketPlayInSetCreativeSlot";

            NbtChecks.checkPacketPlayIn(slot, tag, itemClass, packetClass, panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSetSlot) {
            PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) _packet;

            int windowId = packet.a();

            // check if window is not player inventory and we are ignoring non-player inventories
            if (windowId != 0 && panilla.getPConfig().ignoreNonPlayerInventories) {
                return;
            }

            int slot = packet.a();

            ItemStack itemStack = packet.e();

            if (itemStack == null || !itemStack.u()) {
                return;
            }

            NbtTagCompound tag = new NbtTagCompound(itemStack.w());
            String itemClass = itemStack.getClass().getSimpleName();
            String packetClass = packet.getClass().getSimpleName();

            NbtChecks.checkPacketPlayOut(slot, tag, itemClass, packetClass, panilla);
        }
    }

    @Override
    public void checkPacketPlayOutWindowItems(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutWindowItems) {
            PacketPlayOutWindowItems packet = (PacketPlayOutWindowItems) _packet;

            int windowId = packet.a();

            // check if window is not player inventory
            if (windowId != 0) {
                return;
            }

            List<ItemStack> itemStacks = packet.d();

            for (ItemStack itemStack : itemStacks) {
                if (!itemStack.u()) {
                    continue;
                }

                NbtTagCompound tag = new NbtTagCompound(itemStack.w());
                String itemClass = itemStack.getClass().getSimpleName();
                String packetClass = packet.getClass().getSimpleName();

                NbtChecks.checkPacketPlayOut(0, tag, itemClass, packetClass, panilla); // TODO: set slot?
            }
        }
    }

    @Override
    public void checkPacketPlayOutSpawnEntity(Object _packet) throws EntityNbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSpawnEntity) {
            PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) _packet;

            UUID entityId = packet.d();
            Entity entity = null;

            for (WorldServer worldServer : MinecraftServer.getServer().H()) {
                entity = paperChunkSystem ? getChunkSystemEntity(worldServer, entityId) : worldServer.M.d().a(entityId);
                if (entity != null) break;
            }

            if (entity != null) {
                if (entity instanceof EntityItem) {
                    EntityItem item = (EntityItem) entity;

                    if (item.q() == null) {
                        return;
                    }

                    if (!item.q().u()) {
                        return;
                    }

                    INbtTagCompound tag = new NbtTagCompound(item.q().w());
                    String itemName = item.q().d().a();
                    String worldName = "";

                    try {
                        Field worldField = Entity.class.getDeclaredField("t");
                        worldField.setAccessible(true);
                        World world = (World) worldField.get(entity);
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
            }
        }
    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(entityPlayer.bR.j, entityPlayer.bR.k(), slot, new ItemStack(Blocks.a));
        entityPlayer.c.b(packet);
    }

    @Override
    public void stripNbtFromItemEntity(UUID entityId) {
        Entity entity = null;

        for (WorldServer worldServer : MinecraftServer.getServer().H()) {
            entity = paperChunkSystem ? getChunkSystemEntity(worldServer, entityId) : worldServer.M.d().a(entityId);
            if (entity != null) break;
        }

        if (entity instanceof EntityItem) {
            EntityItem item = (EntityItem) entity;
            if (item.q() == null) return;
            if (!item.q().u()) return;
            item.q().c((NBTTagCompound) null);
        }
    }

    @Override
    public void stripNbtFromItemEntityLegacy(int entityId) {
        throw new RuntimeException("cannot use #stripNbtFromItemEntityLegacy on 1.20.2-4");
    }

    @Override
    public void validateBaseComponentParse(String string) throws Exception {
        IChatBaseComponent.ChatSerializer.a(string);
    }

}
