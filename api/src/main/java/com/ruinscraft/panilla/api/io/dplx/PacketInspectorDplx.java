package com.ruinscraft.panilla.api.io.dplx;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.exception.RateLimitException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketInspectorDplx extends ChannelDuplexHandler {

    private final IPanilla panilla;
    private final IPanillaPlayer player;

    public PacketInspectorDplx(IPanilla panilla, IPanillaPlayer player) {
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
        } catch (RateLimitException e) {
            handleRateLimitException(player, e);
            return;
        }

        super.channelRead(ctx, msg);
    }

    // server -> player
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            panilla.getPacketInspector().checkPlayOut(panilla, msg);
        } catch (PacketException e) {
            if (handlePacketException(player, e)) {
                return;
            }
        }

        super.write(ctx, msg, promise);
    }

    private boolean handlePacketException(IPanillaPlayer player, PacketException e) {
        if (!player.canBypassChecks(panilla, e)) {
            panilla.getInventoryCleaner().clean(player);

            IPanillaLogger panillaLogger = panilla.getPanillaLogger();
            PTranslations pTranslations = panilla.getPTranslations();

            String nmsClass = e.getNmsClass();
            String username = player.getName();
            String tag;

            if (FailedNbt.failsThreshold(e.getFailedNbt())) {
                tag = "key size threshold";
            } else {
                tag = e.getFailedNbt().key;
            }

            final String message;

            if (e.isFrom()) {
                message = pTranslations.getTranslation("packetFromDropped", nmsClass, username, tag);
            } else {
                message = pTranslations.getTranslation("packetToDropped", nmsClass, username, tag);
            }

            panillaLogger.log(message, true);

            return true;
        }

        return false;
    }

    private void handleRateLimitException(IPanillaPlayer player, RateLimitException e) {
        IPanillaLogger panillaLogger = panilla.getPanillaLogger();
        PTranslations pTranslations = panilla.getPTranslations();
        String message = pTranslations.getTranslation("packetRateLimitExceeded", e.getPacketClassName(), e.getPPS(), player.getName());

        panillaLogger.log(message, true);
    }

}
