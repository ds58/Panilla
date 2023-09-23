package com.ruinscraft.panilla.craftbukkit.v1_20_R2.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.channel.Channel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;

import java.lang.reflect.Field;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection playerConnection = entityPlayer.c;

        try {
            Field networkManagerField = PlayerConnection.class.getSuperclass().getDeclaredField("c");
            networkManagerField.setAccessible(true);
            NetworkManager networkManager = (NetworkManager) networkManagerField.get(playerConnection);
            return networkManager.n;
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
        throw new RuntimeException("Not implemented");
    }

}
