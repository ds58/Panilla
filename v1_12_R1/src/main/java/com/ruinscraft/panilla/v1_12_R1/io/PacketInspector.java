package com.ruinscraft.panilla.v1_12_R1.io;

import java.io.IOException;
import java.lang.reflect.Field;

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
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;

public class PacketInspector implements IPacketInspector {

	private final PStrictness strictness;
	private final IProtocolConstants protocolConstants;
	private final INbtChecker nbtChecker;

	public PacketInspector(PStrictness strictness,
			IProtocolConstants protocolConstants,
			INbtChecker nbtChecker) {
		this.strictness = strictness;
		this.protocolConstants = protocolConstants;
		this.nbtChecker = nbtChecker;
	}

	@Override
	public void checkSize(Object _packet, boolean from) throws OversizedPacketException {
		if (_packet instanceof Packet<?>) {
			Packet<?> packet = (Packet<?>) _packet;
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
			
			nbtChecker.checkPlayIn(itemStack.getTag(),
					itemStack.getItem().getClass().getSimpleName(),
					strictness,
					packet.getClass().getSimpleName());
		}
	}

	@Override
	public void checkPacketPlayInUpdateSign(Object _packet) throws SignLineLengthTooLongException {
		if (_packet instanceof PacketPlayInUpdateSign) {
			PacketPlayInUpdateSign packet = (PacketPlayInUpdateSign) _packet;

			for (String line : packet.b()) {
				if (line.length() > protocolConstants.maxSignLineLength()) {
					throw new SignLineLengthTooLongException(packet.getClass().getSimpleName(), true);
				}
			}
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

				nbtChecker.checkPlayOut(itemStack.getTag(),
						itemStack.getItem().getClass().getSimpleName(),
						strictness,
						packet.getClass().getSimpleName());
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
