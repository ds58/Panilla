package com.ruinscraft.panilla.craftbukkit.v1_8_R3;

import com.ruinscraft.panilla.api.IProtocolConstants;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class ProtocolConstants implements IProtocolConstants {

    @Override
    public int packetCompressionLevel() {
        return MinecraftServer.getServer().aK();
    }

    @Override
    public int maxPacketSizeBytes() {
        return 2097152; // net.minecraft.server.PacketDecompressor L33
    }

}
