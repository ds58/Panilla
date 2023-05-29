package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import io.netty.channel.Channel;

public interface IPlayerInjector {

    // channels : timeout,decrypt,splitter,decompress,decoder,encrypt,prepender,compress,encoder,packet_handler,DefaultChannelPipeline$TailContext#0

    String PANILLA_CHANNEL_IN = "panilla_in";
    String PANILLA_CHANNEL_OUT = "panilla_out";
    String BYPASS_PERMISSION = "panilla.bypass";

    Channel getPlayerChannel(IPanillaPlayer player);

    default void register(IPanillaPlayer player, IPanilla panilla) {
        Channel channel = getPlayerChannel(player);

        System.out.println("names: " + String.join(",", channel.pipeline().names()));

        /* Register inbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_IN) == null) {
            PlayerInbound inbound = new PlayerInbound(player, panilla);
            channel.pipeline().addFirst(PANILLA_CHANNEL_IN, inbound);
        }

        /* Register outbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_OUT) == null) {
            PlayerOutbound outbound = new PlayerOutbound(player, panilla);
            channel.pipeline().addFirst(PANILLA_CHANNEL_OUT, outbound);
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);

        /* Unregister inbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_IN) != null) {
            channel.pipeline().remove(PANILLA_CHANNEL_IN);
        }

        /* Unregister outbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_OUT) != null) {
            channel.pipeline().remove(PANILLA_CHANNEL_OUT);
        }
    }

    static boolean canBypass(IPanillaPlayer player) {
        return player.isOp() || player.hasPermission(BYPASS_PERMISSION);
    }

}
