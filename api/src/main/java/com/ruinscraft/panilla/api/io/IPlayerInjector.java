package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.dplx.PacketInspectorDplx;
import io.netty.channel.Channel;

public interface IPlayerInjector {

    String CHANNEL_DECOMPRESSOR_PANILLA = "panilla_decompressor";
    String CHANNEL_INSPECTOR_PANILLA = "panilla_inspector";
    String CHANNEL_EXCEPTION_PANILLA = "panilla_exception";

    Channel getPlayerChannel(IPanillaPlayer player);

    default String getDecompressorChannelName() {
        return "decompress";
    }

    default String getPacketHandlerChannelName() {
        return "packet_handler";
    }

    default void register(IPanilla panilla, IPanillaPlayer player) {
        Channel pChannel = getPlayerChannel(player);

        if (pChannel == null) {
            return;
        }

        if (pChannel.pipeline().get(getDecompressorChannelName()) != null) {
            /* Inject Panilla decompressor */
            pChannel.pipeline().addBefore(getDecompressorChannelName(), CHANNEL_DECOMPRESSOR_PANILLA, null);
            /* Remove Minecraft decompressor */
            pChannel.pipeline().remove(getDecompressorChannelName());
        }

        if (pChannel.pipeline().get(getPacketHandlerChannelName()) != null) {
            PacketInspectorDplx channel = new PacketInspectorDplx(panilla, player);
            pChannel.pipeline().addBefore(getPacketHandlerChannelName(), CHANNEL_INSPECTOR_PANILLA, channel);
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);
        if (channel == null) return;
        if (channel.pipeline().get(CHANNEL_INSPECTOR_PANILLA) != null) {
            channel.pipeline().remove(CHANNEL_INSPECTOR_PANILLA);
        }
        if (channel.pipeline().get(CHANNEL_EXCEPTION_PANILLA) != null) {
            channel.pipeline().remove(CHANNEL_EXCEPTION_PANILLA);
        }
    }

}
