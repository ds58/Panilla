package com.ruinscraft.panilla.glowstone.r2018_9_0.io.dplx;

import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;

public class PacketSerializer implements IPacketSerializer {

    @Override
    public int readableBytes() {
        return 0;
    }

    @Override
    public int readVarInt() {
        return 0;
    }

    @Override
    public ByteBuf readBytes(int i) {
        return null;
    }

    @Override
    public ByteBuf readBytes(byte[] buffer) {
        return null;
    }

}
