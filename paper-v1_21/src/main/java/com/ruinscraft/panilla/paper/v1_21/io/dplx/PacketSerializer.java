package com.ruinscraft.panilla.paper.v1_21.io.dplx;

import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketDataSerializer;

public class PacketSerializer implements IPacketSerializer {

    private final PacketDataSerializer handle;

    public PacketSerializer(ByteBuf byteBuf) {
        this.handle = new PacketDataSerializer(byteBuf);
    }

    @Override
    public int readableBytes() {
        return handle.readableBytes();
    }

    @Override
    public int readVarInt() {
        return handle.l();
    }

    @Override
    public ByteBuf readBytes(int i) {
        return handle.readBytes(i);
    }

    @Override
    public ByteBuf readBytes(byte[] buffer) {
        return handle.readBytes(buffer);
    }

}
