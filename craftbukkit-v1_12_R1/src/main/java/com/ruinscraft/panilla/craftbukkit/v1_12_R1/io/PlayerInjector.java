package com.ruinscraft.panilla.craftbukkit.v1_12_R1.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.channel.Channel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketDecompressor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        CraftPlayer craftPlayer = (CraftPlayer) player.getHandle();
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.playerConnection.networkManager.channel;
    }

    @Override
    public int getCompressionLevel() {
        return MinecraftServer.getServer().aG();    // TODO: check this mapping
    }

    @Override
    public ByteToMessageDecoder getDecompressor() {
        return new PacketDecompressor(getCompressionLevel());
    }

}
