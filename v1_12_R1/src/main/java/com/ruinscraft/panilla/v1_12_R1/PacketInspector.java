package com.ruinscraft.panilla.v1_12_R1;

import java.io.IOException;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.flowpowered.nbt.CompoundTag;
import com.ruinscraft.panilla.api.exception.ModifiedItemStackException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IProtocolConstants;

import io.netty.buffer.UnpooledByteBufAllocator;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot;

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

			NBTTagCompound nbtTagCompound = itemStack.getTag();

			
		}
	}

}
