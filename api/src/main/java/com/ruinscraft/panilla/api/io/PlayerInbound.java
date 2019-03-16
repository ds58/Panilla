package com.ruinscraft.panilla.api.io;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.PacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final Player player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final IProtocolConstants protocolConstants;
	private final PConfig config;
	private final PanillaLogger panillaLogger;

	public PlayerInbound(Player player, IPacketInspector packetInspector, IContainerCleaner containerCleaner,
			IProtocolConstants protocolConstants, PConfig config, PanillaLogger panillaLogger) {
		this.player = player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.protocolConstants = protocolConstants;
		this.config = config;
		this.panillaLogger = panillaLogger;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			packetInspector.checkPlayIn(msg);
		} catch (PacketException e) {
			containerCleaner.clean(player);
			panillaLogger.warn(player, e, protocolConstants, config);

			return; // drop the packet
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.channelRead(ctx, msg);
	}

}
