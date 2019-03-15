package com.ruinscraft.panilla.v1_13_R2.io;

import java.io.IOException;
import java.lang.reflect.Field;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.v1_13_R2.nbt.NbtTagCompound;

import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketDataSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayInCustomPayload;
import net.minecraft.server.v1_13_R2.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_13_R2.PacketPlayInUpdateSign;
import net.minecraft.server.v1_13_R2.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_13_R2.PacketPlayOutSetSlot;

public class PacketInspector implements IPacketInspector {

	private final PStrictness strictness;
	private final IProtocolConstants protocolConstants;

	public PacketInspector(PStrictness strictness,
			IProtocolConstants protocolConstants) {
		this.strictness = strictness;
		this.protocolConstants = protocolConstants;
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

			NbtChecks.checkPacketPlayIn(new NbtTagCompound(itemStack.getTag()),
					itemStack.getItem().getClass().getSimpleName(),
					packet.getClass().getSimpleName(), 
					protocolConstants, 
					strictness);
		}
	}

	@Override
	public void checkPacketPlayInUpdateSign(Object _packet) throws SignLineLengthTooLongException {
		if (_packet instanceof PacketPlayInUpdateSign) {
			PacketPlayInUpdateSign packet = (PacketPlayInUpdateSign) _packet;

			for (String line : packet.c()) {
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

				NbtChecks.checkPacketPlayOut(new NbtTagCompound(itemStack.getTag()),
						itemStack.getItem().getClass().getSimpleName(),
						packet.getClass().getSimpleName(), 
						protocolConstants, 
						strictness);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
