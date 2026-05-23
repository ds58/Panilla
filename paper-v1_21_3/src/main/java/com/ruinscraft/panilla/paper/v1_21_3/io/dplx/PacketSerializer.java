package com.ruinscraft.panilla.paper.v1_21_3.io.dplx;

import com.ruinscraft.panilla.api.io.IPacketSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class PacketSerializer implements IPacketSerializer {

    private final FriendlyByteBuf handle;

    public PacketSerializer(ByteBuf byteBuf) {
        this.handle = new FriendlyByteBuf(byteBuf);
    }

    @Override
    public int readableBytes() {
        return handle.readableBytes();
    }

    @Override
    public int readVarInt() {
        return handle.readVarInt();
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
