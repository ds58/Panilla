package com.ruinscraft.panilla.forge112.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.channel.Channel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;

import java.lang.reflect.Field;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player.getHandle();
        try {
            Field channelField = NetworkManager.class.getField("connection");
            channelField.setAccessible(true);
            Channel channel = (Channel) channelField.get(entityPlayerMP.connection.netManager);
            return channel;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCompressionLevel() {
        return 0;   // TODO:
    }

    @Override
    public ByteToMessageDecoder getDecompressor() {
        return null;   // TODO:
    }

    @Override
    public ByteToMessageDecoder getDecoder() {
        return null;
    }

}
