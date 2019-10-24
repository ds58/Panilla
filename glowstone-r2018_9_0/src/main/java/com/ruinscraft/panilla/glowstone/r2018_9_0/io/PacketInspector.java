package com.ruinscraft.panilla.glowstone.r2018_9_0.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.LegacyEntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.glowstone.r2018_9_0.nbt.GlowNbtHelper;
import com.ruinscraft.panilla.glowstone.r2018_9_0.nbt.NbtTagCompound;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.objects.GlowItem;
import net.glowstone.inventory.GlowMetaItem;
import net.glowstone.net.message.play.entity.SpawnMobMessage;
import net.glowstone.net.message.play.inv.CreativeItemMessage;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PacketInspector implements IPacketInspector {

    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof CreativeItemMessage) {
            CreativeItemMessage packet = (CreativeItemMessage) _packet;

            final int slot = packet.getSlot();
            final ItemStack itemStack = packet.getItem();

            if (itemStack == null || !(itemStack.getItemMeta() instanceof GlowMetaItem)) {
                return;
            }

            GlowMetaItem meta = (GlowMetaItem) itemStack.getItemMeta();
            CompoundTag ngTag = GlowNbtHelper.getNbt(meta);

            NbtChecks.checkPacketPlayIn(slot, new NbtTagCompound(ngTag), itemStack.getType().name(), packet.getClass().getSimpleName(), panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof SetWindowSlotMessage) {
            SetWindowSlotMessage packet = (SetWindowSlotMessage) _packet;

            final int slot = packet.getSlot();
            final ItemStack itemStack = packet.getItem();

            if (itemStack == null || !(itemStack.getItemMeta() instanceof GlowMetaItem)) {
                return;
            }

            GlowMetaItem meta = (GlowMetaItem) itemStack.getItemMeta();
            CompoundTag ngTag = GlowNbtHelper.getNbt(meta);

            NbtChecks.checkPacketPlayIn(slot, new NbtTagCompound(ngTag), itemStack.getType().name(), packet.getClass().getSimpleName(), panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSpawnEntity(Object _packet) throws EntityNbtNotPermittedException, LegacyEntityNbtNotPermittedException {
        if (_packet instanceof SpawnMobMessage) {
            SpawnMobMessage packet = (SpawnMobMessage) _packet;

            UUID entityId = packet.getUuid();
            Entity bukkitEntity = Bukkit.getEntity(entityId);
            GlowEntity glowEntity = (GlowEntity) bukkitEntity;

            if (glowEntity instanceof GlowItem) {
                GlowItem glowItem = (GlowItem) glowEntity;
                ItemStack itemStack = glowItem.getItemStack();

                if (itemStack == null) {
                    return;
                }

                if (!(itemStack.getItemMeta() instanceof GlowMetaItem)) {
                    return;
                }

                GlowMetaItem meta = (GlowMetaItem) glowItem.getItemStack().getItemMeta();
                CompoundTag ngTag = GlowNbtHelper.getNbt(meta);
                INbtTagCompound tag = new NbtTagCompound(ngTag);
                FailedNbt failedNbt = NbtChecks.checkAll(tag, glowItem.getItemStack().getClass().getSimpleName(), panilla);

                if (FailedNbt.fails(failedNbt)) {
                    throw new EntityNbtNotPermittedException(packet.getClass().getSimpleName(), false, failedNbt, entityId);
                }
            }
        }
    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {

    }

    @Override
    public void removeEntity(UUID entityId) {
        Bukkit.getEntity(entityId).remove();
    }

    @Override
    public void removeEntityLegacy(int entityId) {
        throw new IllegalStateException("Cannot use #removeEntityLegacy on 1.12");
    }

}
