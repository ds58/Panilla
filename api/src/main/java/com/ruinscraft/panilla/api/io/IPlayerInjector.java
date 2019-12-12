package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.dplx.PacketDecompressorDplx;
import com.ruinscraft.panilla.api.io.dplx.PacketInspectorDplx;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;

public interface IPlayerInjector {

    String HANDLER_PANILLA_INSPECTOR = "panilla_inspector";

    Channel getPlayerChannel(IPanillaPlayer player);

    int getCompressionLevel();

    ByteToMessageDecoder getDecompressor();

    default String getDecompressorHandlerName() {
        return "decompress";
    }

    default String getPacketHandlerName() {
        return "packet_handler";
    }

    default void register(IPanilla panilla, IPanillaPlayer player) {
        Channel pChannel = getPlayerChannel(player);

        if (pChannel == null) {
            return;
        }

        /* Replace Minecraft packet decompressor */
        ChannelHandler defaultChannel = pChannel.pipeline().get(getDecompressorHandlerName());

        if (defaultChannel != null && !(defaultChannel instanceof PacketDecompressorDplx)) {
            PacketDecompressorDplx packetDecompressor = new PacketDecompressorDplx(panilla, player);
            pChannel.pipeline().replace(getDecompressorHandlerName(), getDecompressorHandlerName(), packetDecompressor);
        }

        /* Inject packet inspector */
        if (!(pChannel.pipeline().get(getPacketHandlerName()) instanceof PacketInspectorDplx)) {
            PacketInspectorDplx packetInspector = new PacketInspectorDplx(panilla, player);
            pChannel.pipeline().addBefore(getPacketHandlerName(), HANDLER_PANILLA_INSPECTOR, packetInspector);
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel pChannel = getPlayerChannel(player);

        if (pChannel == null) {
            return;
        }

        /* Replace Panilla packet decompressor with the default */
        if (pChannel.pipeline().get(getDecompressorHandlerName()) instanceof PacketDecompressorDplx) {
            ByteToMessageDecoder defaultDecompressor = getDecompressor();
            pChannel.pipeline().replace(getDecompressorHandlerName(), getDecompressorHandlerName(), defaultDecompressor);
        }

        /* Remove packet inspector */
        if (pChannel.pipeline().get(HANDLER_PANILLA_INSPECTOR) instanceof PacketInspectorDplx) {
            pChannel.pipeline().remove(HANDLER_PANILLA_INSPECTOR);
        }
    }

}
