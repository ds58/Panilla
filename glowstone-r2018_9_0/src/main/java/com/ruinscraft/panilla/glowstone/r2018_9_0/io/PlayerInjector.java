package com.ruinscraft.panilla.glowstone.r2018_9_0.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import io.netty.channel.Channel;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;

public class PlayerInjector implements IPlayerInjector {

    @Override
    public Channel getPlayerChannel(IPanillaPlayer player) {
        GlowPlayer glowPlayer = (GlowPlayer) player.getHandle();
        GlowSession glowSession = glowPlayer.getSession();
        return glowSession.getChannel();
    }

    @Override
    public String getDecompressorHandlerName() {
        return "";  // TODO:
    }

    @Override
    public int getCompressionLevel() {
        return 0;   // TODO:
    }

    @Override
    public ByteToMessageDecoder getDecompressor() {
        return null;    // TODO:
    }

    @Override
    public ByteToMessageDecoder getDecoder() {
        return null;
    }

}
