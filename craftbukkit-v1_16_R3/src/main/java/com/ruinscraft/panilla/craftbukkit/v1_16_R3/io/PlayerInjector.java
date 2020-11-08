package com.ruinscraft.panilla.craftbukkit.v1_16_R3.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumProtocolDirection;
import net.minecraft.server.v1_16_R3.PacketDecoder;
import net.minecraft.server.v1_16_R3.PacketDecompressor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

import java.util.List;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.playerConnection.networkManager.channel;
    }

    @Override
    public int getCompressionLevel() {
        return 256;
    }

    @Override
    public ByteToMessageDecoder getDecompressor() {
        return new PacketDecompressor(getCompressionLevel());
    }

    @Override
    public ByteToMessageDecoder getDecoder() {
        return new PanillaPacketDecoder(EnumProtocolDirection.SERVERBOUND);
    }

    private class PanillaPacketDecoder extends PacketDecoder {
        public PanillaPacketDecoder(EnumProtocolDirection enumProtocolDirection) {
            super(enumProtocolDirection);
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
            try {
                super.decode(channelHandlerContext, byteBuf, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
