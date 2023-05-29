package com.ruinscraft.panilla.craftbukkit.v1_19_R3.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.List;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection playerConnection = entityPlayer.b;

        try {
            Field networkManagerField = PlayerConnection.class.getDeclaredField("h");
            networkManagerField.setAccessible(true);
            NetworkManager networkManager = (NetworkManager) networkManagerField.get(playerConnection);
            return networkManager.m;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCompressionLevel() {
        return 256;
    }

    @Override
    public ByteToMessageDecoder getDecompressor() {
        return null;
    }

    @Override
    public ByteToMessageDecoder getDecoder() {
        return new PanillaPacketDecoder(EnumProtocolDirection.a);
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
