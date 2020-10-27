package com.ruinscraft.panilla.craftbukkit.v1_8_R3.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

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
        return MinecraftServer.getServer().aK();    // TODO: check this mapping
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
