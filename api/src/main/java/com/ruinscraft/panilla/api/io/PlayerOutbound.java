package com.ruinscraft.panilla.api.io;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.exception.PacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter {

	private final Player player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final PanillaLogger panillaLogger;

	public PlayerOutbound(Player player, IPacketInspector packetInspector, IContainerCleaner containerCleaner,
			PanillaLogger panillaLogger) {
		this.player = player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.panillaLogger = panillaLogger;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			packetInspector.checkPlayOut(msg);
		} catch (PacketException e) {
			containerCleaner.clean(player);
			panillaLogger.warn(player, e);

			return; // drop the packet
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.write(ctx, msg, promise);
	}

}
