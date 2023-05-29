package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.io.dplx.PacketDecompressorDplx;
import com.ruinscraft.panilla.api.io.dplx.PacketInspectorDplx;
import io.netty.channel.Channel;

public interface IPlayerInjector {

    String HANDLER_PANILLA_INSPECTOR = "panilla_inspector";

    Channel getPlayerChannel(IPanillaPlayer player);

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
        if (pChannel.pipeline().get(getDecompressorHandlerName()) != null) {
            /* Inject Panilla decompressor (Panilla uses the same channel name as the vanilla decompressor) */
            PacketDecompressorDplx packetDecompressor = new PacketDecompressorDplx(panilla, player);
            pChannel.pipeline().addBefore(getDecompressorHandlerName(), getDecompressorHandlerName(), packetDecompressor);
            /* Remove Minecraft decompressor */
            pChannel.pipeline().remove(getDecompressorHandlerName());
        }

        /* Inject packet inspector */
        if (pChannel.pipeline().get(getPacketHandlerName()) != null) {
            PacketInspectorDplx packetInspector = new PacketInspectorDplx(panilla, player);
            pChannel.pipeline().addBefore(getPacketHandlerName(), HANDLER_PANILLA_INSPECTOR, packetInspector);
        }
    }

    default void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);

        if (channel == null) {
            return;
        }

        /* Remove packet decompressor */
        if (channel.pipeline().get(HANDLER_PANILLA_DECOMPRESSOR) != null) {
            channel.pipeline().remove(HANDLER_PANILLA_DECOMPRESSOR);
        }

        /* Remove packet inspector */
        if (channel.pipeline().get(HANDLER_PANILLA_INSPECTOR) != null) {
            channel.pipeline().remove(HANDLER_PANILLA_INSPECTOR);
        }
    }

}
