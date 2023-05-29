package com.ruinscraft.panilla.forge112.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.forge112.nbt.NbtTagCompound;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.server.SPacketSetSlot;

import java.lang.reflect.Field;

public class PacketInspector implements IPacketInspector {

    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof CPacketCreativeInventoryAction) {
            CPacketCreativeInventoryAction packet = (CPacketCreativeInventoryAction) _packet;

            int slot = packet.getSlotId();
            ItemStack itemStack = packet.getStack();

            if (itemStack == null || !itemStack.hasTagCompound()) return;

            NbtChecks.checkPacketPlayIn(slot, new NbtTagCompound(
                            itemStack.getTagCompound()), itemStack.getItem().getClass().getSimpleName(),
                    packet.getClass().getSimpleName(), panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof SPacketSetSlot) {
            SPacketSetSlot packet = (SPacketSetSlot) _packet;

            try {
                Field slotField = SPacketSetSlot.class.getDeclaredField("slot");
                Field itemStackField = SPacketSetSlot.class.getDeclaredField("item");

                slotField.setAccessible(true);
                itemStackField.setAccessible(true);

                int slot = (int) slotField.get(packet);
                ItemStack itemStack = (ItemStack) itemStackField.get(packet);

                if (itemStack == null || !itemStack.hasTagCompound()) return;

                NbtChecks.checkPacketPlayOut(slot, new NbtTagCompound(
                                itemStack.getTagCompound()), itemStack.getItem().getClass().getSimpleName(),
                        packet.getClass().getSimpleName(), panilla);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player.getHandle();
        SPacketSetSlot packet = new SPacketSetSlot(0, slot, new ItemStack(Blocks.AIR));
        entityPlayerMP.connection.sendPacket(packet);
    }

}
