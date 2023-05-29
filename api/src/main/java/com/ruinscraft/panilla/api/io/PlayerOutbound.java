package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.PacketException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter {

    private final Player player;
    private final IPacketInspector packetInspector;
    private final IContainerCleaner containerCleaner;
    private final IProtocolConstants protocolConstants;
    private final PConfig config;
    private final PanillaLogger panillaLogger;

    // a cache for permission checking
    private short packetsSinceBypassCheck = 0;
    private boolean bypass;

    public PlayerOutbound(Player player, IPacketInspector packetInspector, IContainerCleaner containerCleaner,
                          IProtocolConstants protocolConstants, PConfig config, PanillaLogger panillaLogger) {
        this.player = player;
        this.packetInspector = packetInspector;
        this.containerCleaner = containerCleaner;
        this.protocolConstants = protocolConstants;
        this.config = config;
        this.panillaLogger = panillaLogger;

        bypass = IPlayerInjector.canBypass(player);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (++packetsSinceBypassCheck > 64) {
            packetsSinceBypassCheck = 0;
            bypass = IPlayerInjector.canBypass(player);
        }

        if (!bypass) {
            try {
                packetInspector.checkPlayOut(msg);
            } catch (PacketException e) {
                containerCleaner.clean(player);
                panillaLogger.warn(player, e, protocolConstants, config);

                return; // drop the packet
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.write(ctx, msg, promise);
    }

}
