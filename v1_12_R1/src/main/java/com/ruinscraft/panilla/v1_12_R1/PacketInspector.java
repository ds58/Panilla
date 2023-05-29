package com.ruinscraft.panilla.v1_12_R1;

import java.io.IOException;

import com.ruinscraft.panilla.api.IPacketInspector;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;

import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

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

			try {
				packet.b(dataSerializer);
			} catch (IOException e) {
				e.printStackTrace();
			}

			final int sizeBytes = dataSerializer.readableBytes();

			if (sizeBytes > protocolConstants.packetMaxBytes()) {
				throw new OversizedPacketException(packet.getClass().getName(), sizeBytes);
			}

			dataSerializer.release();
		}
	}

}
