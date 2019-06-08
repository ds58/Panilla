package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import io.netty.channel.Channel;

public interface IPlayerInjector {

    String CHANNEL_HANDLER_MINECRAFT = "packet_handler";
    String CHANNEL_HANDLER_PANILLA = "panilla_handler";

    Channel getPlayerChannel(IPanillaPlayer player);

    default void register(IPanilla panilla, IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) == null) {
            PanillaChannelHandler channelHandler = new PanillaChannelHandler(panilla, player);
            if (channel.pipeline().get(CHANNEL_HANDLER_MINECRAFT) != null) {
                channel.pipeline().addBefore(CHANNEL_HANDLER_MINECRAFT, CHANNEL_HANDLER_PANILLA, channelHandler);
            } else {
                System.out.println("no packet_handler channel!");
            }
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) != null) {
            channel.pipeline().remove(CHANNEL_HANDLER_PANILLA);
        }
    }

}
