package com.ruinscraft.panilla.craftbukkit.v1_14_R1;

import com.ruinscraft.panilla.api.IProtocolConstants;
import net.minecraft.server.v1_14_R1.MinecraftServer;

public class ProtocolConstants implements IProtocolConstants {

    @Override
    public int packetCompressionLevel() {
        return MinecraftServer.getServer().ay();
    }

    @Override
    public int maxPacketSizeBytes() {
        return 2097152; // net.minecraft.server.PacketDecompressor L33
    }

}
