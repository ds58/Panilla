package com.ruinscraft.panilla.sponge72.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.sponge72.nbt.NbtTagCompound;
import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketSetSlot;

import java.io.IOException;
import java.lang.reflect.Field;

public class PacketInspector implements IPacketInspector {

    private final IPanilla panilla;

    public PacketInspector(IPanilla panilla) {
        this.panilla = panilla;
    }

    @Override
    public void checkSize(Object _packet, boolean from) throws OversizedPacketException {
        if (_packet instanceof Packet<?>) {
            Packet<?> packet = (Packet<?>) _packet;
            PacketBuffer dataSerializer = new PacketBuffer(UnpooledByteBufAllocator.DEFAULT.buffer());

            int sizeBytes = 0;

            try {
                packet.readPacketData(dataSerializer);

                sizeBytes = dataSerializer.readableBytes();

                // https://github.com/aadnk/ProtocolLib/commit/5ec87c9d7650ae21faca9b7b3cc7ac1629870d24
                if (packet instanceof CPacketCustomPayload || packet instanceof SPacketCustomPayload) {
                    packet.writePacketData(dataSerializer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                dataSerializer.release();
            }

            if (sizeBytes > panilla.getProtocolConstants().maxPacketSizeBytes()) {
                throw new OversizedPacketException(packet.getClass().getSimpleName(), from, sizeBytes);
            }
        }
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
        // int          windowId (0 for player)
        // int          slotId
        // ItemStack    item
        SPacketSetSlot packet = new SPacketSetSlot(0, slot, new ItemStack(Block.getBlockById(0)));
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player.getHandle();
        entityPlayerMP.connection.sendPacket(packet);
    }

}
