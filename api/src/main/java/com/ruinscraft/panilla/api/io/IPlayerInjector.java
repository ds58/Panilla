package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import io.netty.channel.Channel;

public interface IPlayerInjector {

    String CHANNEL_HANDLER_MINECRAFT = "packet_handler";
    String CHANNEL_HANDLER_PANILLA = "panilla_handler";
    String CHANNEL_EXCEPTION_PANILLA = "panilla_exception";

    Channel getPlayerChannel(IPanillaPlayer player);

    default void register(IPanilla panilla, IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel == null) return;
        if (channel.pipeline().get(CHANNEL_HANDLER_MINECRAFT) == null) return;
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) != null) return;
        channel.pipeline().addBefore(CHANNEL_HANDLER_MINECRAFT, CHANNEL_HANDLER_PANILLA, new PanillaChannelHandler(panilla, player));
        channel.pipeline().addLast(CHANNEL_EXCEPTION_PANILLA, new PanillaExceptionHandler(panilla, player));
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel == null) return;
        if (channel.pipeline().get(CHANNEL_HANDLER_PANILLA) == null) return;
        channel.pipeline().remove(CHANNEL_HANDLER_PANILLA);
    }

}
