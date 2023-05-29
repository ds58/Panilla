package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.PacketException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

    private final IPanillaPlayer player;
    private final IPanilla panilla;

    // a cache for permission checking
    private short packetsSinceBypassCheck = 0;
    private boolean bypass;

    public PlayerInbound(IPanillaPlayer player, IPanilla panilla) {
        this.player = player;
        this.panilla = panilla;

        bypass = IPlayerInjector.canBypass(player);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (++packetsSinceBypassCheck > 64) {
                packetsSinceBypassCheck = 0;
                bypass = IPlayerInjector.canBypass(player);
            }

            if (!bypass) {
                try {
                    panilla.getPacketInspector().checkPlayIn(player, msg, panilla);
                } catch (PacketException e) {
                    panilla.getContainerCleaner().clean(player);
                    panilla.getPanlliaLogger().warn(player, e);

                    return; // drop the packet
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            super.channelRead(ctx, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
