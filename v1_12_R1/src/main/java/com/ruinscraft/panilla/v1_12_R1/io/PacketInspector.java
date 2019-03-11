package com.ruinscraft.panilla.v1_12_R1.io;

import java.io.IOException;

import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;
import com.ruinscraft.panilla.api.io.IPacketInspector;

import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayInCustomPayload;
import net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_12_R1.PacketPlayInUpdateSign;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

public class PacketInspector implements IPacketInspector {

	private final PStrictness strictness;
	private final IProtocolConstants protocolConstants;
	private final INbtChecker nbtChecker;

	public PacketInspector(PStrictness strictness, IProtocolConstants protocolConstants, INbtChecker nbtChecker) {
		this.strictness = strictness;
		this.protocolConstants = protocolConstants;
		this.nbtChecker = nbtChecker;
	}

	@Override
	public void checkSize(Object player, Object nmsPacket) throws OversizedPacketException {
		if (nmsPacket instanceof Packet<?>) {
			Packet<?> packet = (Packet<?>) nmsPacket;
			PacketDataSerializer dataSerializer = 
					new PacketDataSerializer(UnpooledByteBufAllocator.DEFAULT.buffer());

			int sizeBytes = 0;

			try {
				packet.b(dataSerializer);

				sizeBytes = dataSerializer.readableBytes();

				// https://github.com/aadnk/ProtocolLib/commit/5ec87c9d7650ae21faca9b7b3cc7ac1629870d24
				if (packet instanceof PacketPlayInCustomPayload
						|| packet instanceof PacketPlayOutCustomPayload) {
					packet.a(dataSerializer);
				}
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
	public void checkPacketPlayInSetCreativeSlot(Object player, Object nmsPacket) throws NbtNotPermittedException {
		if (nmsPacket instanceof PacketPlayInSetCreativeSlot) {
			PacketPlayInSetCreativeSlot packetPlayInSetCreativeSlot = (PacketPlayInSetCreativeSlot) nmsPacket;
			ItemStack itemStack = packetPlayInSetCreativeSlot.getItemStack();
			
			if (itemStack != null && itemStack.hasTag()) {
				nbtChecker.checkAll(itemStack.getTag(), strictness);
			}
		}
	}

	@Override
	public void checkPacketPlayInUpdateSign(Object player, Object nmsPacket) throws SignLineLengthTooLongException {
		if (nmsPacket instanceof PacketPlayInUpdateSign) {
			PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) nmsPacket;
			
			for (String line : packetPlayInUpdateSign.b()) {
				if (line.length() > protocolConstants.maxSignLineLength()) {
					throw new SignLineLengthTooLongException();
				}
			}
		}
	}

}
