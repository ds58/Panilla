package com.ruinscraft.panilla.craftbukkit.v1_16_R2.io.dplx;

import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_16_R2.PacketDataSerializer;

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
        return handle.i();
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
