package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
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
            panilla.getPacketInspector().checkPlayIn(player, msg);
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
        if (!player.canBypassChecks(e)) {
            panilla.getContainerCleaner().clean(player);
            panilla.getPanlliaLogger().warn(player, e);
            return true;
        }
        return false;
    }

}
