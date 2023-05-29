package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.exception.PacketException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PanillaChannelHandler extends ChannelDuplexHandler {

    private final IPanilla panilla;
    private final IPanillaPlayer player;

    public PanillaChannelHandler(IPanilla panilla, IPanillaPlayer player) {
        this.panilla = panilla;
        this.player = player;
    }

    // player -> server
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            panilla.getPacketInspector().checkPlayIn(panilla, player, msg);
        } catch (PacketException e) {
            if (handlePacketException(player, e)) {
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    // server -> player
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            panilla.getPacketInspector().checkPlayOut(msg);
        } catch (PacketException e) {
            if (handlePacketException(player, e)) {
                return;
            }
        }

        super.write(ctx, msg, promise);
    }

    private boolean handlePacketException(IPanillaPlayer player, PacketException e) {
        if (!player.canBypassChecks(panilla, e)) {
            panilla.getContainerCleaner().clean(player);

            IPanillaLogger panillaLogger = panilla.getPanillaLogger();
            PTranslations pTranslations = panilla.getPTranslations();

            if (e.isFrom()) {
                panillaLogger.log(pTranslations.getTranslation("packetFromDropped"), true);
            } else {
                panillaLogger.log(pTranslations.getTranslation("packetToDropped"), true);
            }

            return true;
        }
        return false;
    }

}
