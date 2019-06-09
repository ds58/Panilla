package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

public interface IPlayerInjector {

    String CHANNEL_HANDLER_MINECRAFT = "packet_handler";
    String CHANNEL_HANDLER_PANILLA = "panilla_handler";

    Channel getPlayerChannel(IPanillaPlayer player);

    default void register(IPanilla panilla, IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel == null) return;
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) == null) {
            PanillaChannelHandler channelHandler = new PanillaChannelHandler(panilla, player);
            if (channel.pipeline().get(CHANNEL_HANDLER_MINECRAFT) != null) {
                channel.pipeline().addBefore(CHANNEL_HANDLER_MINECRAFT, CHANNEL_HANDLER_PANILLA, channelHandler);
            } else {
                // try again later, server was likely booting up
                panilla.getPlatform().runTaskLater(TimeUnit.SECONDS.toMillis(10), () -> {
                    register(panilla, player);
                });
            }
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel == null) return;
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) != null) {
            channel.pipeline().remove(CHANNEL_HANDLER_PANILLA);
        }
    }

}
