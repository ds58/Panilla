package com.ruinscraft.panilla.forge112;

import com.ruinscraft.panilla.api.IProtocolConstants;

public class ProtocolConstants implements IProtocolConstants {

    @Override
    public int maxPacketSizeBytes() {
        return 2097152; // net.minecraft.server.PacketDecompressor L33
    }

}
