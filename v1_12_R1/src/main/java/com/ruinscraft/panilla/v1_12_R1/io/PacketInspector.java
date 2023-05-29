package com.ruinscraft.panilla.v1_12_R1.io;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.v1_12_R1.nbt.NbtTagCompound;
import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class PacketInspector implements IPacketInspector {

    private final PConfig config;
    private final IProtocolConstants protocolConstants;

    public PacketInspector(PConfig config, IProtocolConstants protocolConstants) {
        this.config = config;
        this.protocolConstants = protocolConstants;
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

            if (sizeBytes > protocolConstants.maxPacketSizeBytes()) {
                throw new OversizedPacketException(packet.getClass().getSimpleName(), from, sizeBytes);
            }
        }
    }

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot packet = (PacketPlayInSetCreativeSlot) _packet;

            ItemStack itemStack = packet.getItemStack();

            if (itemStack == null || !itemStack.hasTag()) return;

            NbtChecks.checkPacketPlayIn(new NbtTagCompound(itemStack.getTag()),
                    itemStack.getItem().getClass().getSimpleName(), packet.getClass().getSimpleName(),
                    protocolConstants, config);
        }
    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {
        if (_packet instanceof PacketPlayOutSetSlot) {
            PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) _packet;

            try {
                Field itemStackField = packet.getClass().getDeclaredField("c");

                itemStackField.setAccessible(true);

                ItemStack itemStack = (ItemStack) itemStackField.get(packet);

                if (itemStack == null || !itemStack.hasTag()) return;

                NbtChecks.checkPacketPlayOut(new NbtTagCompound(itemStack.getTag()),
                        itemStack.getItem().getClass().getSimpleName(), packet.getClass().getSimpleName(),
                        protocolConstants, config);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
