package com.ruinscraft.panilla.craftbukkit.v1_12_R1.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.craftbukkit.v1_12_R1.nbt.NbtTagCompound;
import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

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
            PacketDataSerializer dataSerializer = new PacketDataSerializer(UnpooledByteBufAllocator.DEFAULT.buffer());

            int sizeBytes = 0;

            try {
                packet.b(dataSerializer);

                sizeBytes = dataSerializer.readableBytes();

                // https://github.com/aadnk/ProtocolLib/commit/5ec87c9d7650ae21faca9b7b3cc7ac1629870d24
                if (packet instanceof PacketPlayInCustomPayload || packet instanceof PacketPlayOutCustomPayload) {
                    packet.a(dataSerializer);
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
        if (_packet instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot packet = (PacketPlayInSetCreativeSlot) _packet;

            int slot = packet.a();
            ItemStack itemStack = packet.getItemStack();

            if (itemStack == null || !itemStack.hasTag()) return;

            NbtChecks.checkPacketPlayIn(slot, new NbtTagCompound(
                            itemStack.getTag()), itemStack.getItem().getClass().getSimpleName(),
                    packet.getClass().getSimpleName(), panilla);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSetSlot) {
            PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) _packet;

            try {
                Field slotField = PacketPlayOutSetSlot.class.getDeclaredField("b");
                Field itemStackField = PacketPlayOutSetSlot.class.getDeclaredField("c");

                slotField.setAccessible(true);
                itemStackField.setAccessible(true);

                int slot = (int) slotField.get(packet);
                ItemStack itemStack = (ItemStack) itemStackField.get(packet);

                if (itemStack == null || !itemStack.hasTag()) return;

                NbtChecks.checkPacketPlayOut(slot, new NbtTagCompound(
                                itemStack.getTag()), itemStack.getItem().getClass().getSimpleName(),
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
        PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, slot, ItemStack.a);
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

}
