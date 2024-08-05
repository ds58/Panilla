package com.ruinscraft.panilla.paper.v1_20_6.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.channel.Channel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        ServerPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.connection.connection.channel;
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
