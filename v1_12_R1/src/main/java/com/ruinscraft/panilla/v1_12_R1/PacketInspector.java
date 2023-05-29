package com.ruinscraft.panilla.v1_12_R1;

import java.io.IOException;

import com.ruinscraft.panilla.api.exception.ModifiedItemStackException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IProtocolConstants;

import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayInCustomPayload;
import net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

public class PacketInspector implements IPacketInspector {

	private final IProtocolConstants protocolConstants;

	public PacketInspector(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}

	@Override
	public void checkSize(Object nmsPacket) throws OversizedPacketException {
		if (nmsPacket instanceof Packet<?>) {
			Packet<?> packet = (Packet<?>) nmsPacket;
			PacketDataSerializer dataSerializer = 
					new PacketDataSerializer(UnpooledByteBufAllocator.DEFAULT.buffer());

			int sizeBytes = 0;

			try {
				packet.b(dataSerializer);

				if (packet instanceof PacketPlayInCustomPayload
						|| packet instanceof PacketPlayOutCustomPayload) {
					packet.a(dataSerializer);
				}
				
				sizeBytes = dataSerializer.readableBytes();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dataSerializer.release();
			}

			if (sizeBytes > protocolConstants.packetMaxBytes()) {
				throw new OversizedPacketException(packet.getClass().getName(), sizeBytes);
			}
		}
	}

	@Override
	public void checkPacketPlayInSetCreativeSlot(Object nmsPacket) throws ModifiedItemStackException {
		if (nmsPacket instanceof PacketPlayInSetCreativeSlot) {
			PacketPlayInSetCreativeSlot packetPlayInSetCreativeSlot = (PacketPlayInSetCreativeSlot) nmsPacket;
			ItemStack itemStack = packetPlayInSetCreativeSlot.getItemStack();

			System.out.println(itemStack.hasTag());
		}
	}

}
