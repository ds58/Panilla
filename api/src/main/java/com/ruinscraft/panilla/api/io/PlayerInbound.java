package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.exception.PacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final Object _player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final PanillaLogger panillaLogger;

	public PlayerInbound(Object _player,
			IPacketInspector packetInspector,
			IContainerCleaner containerCleaner,
			PanillaLogger panillaLogger) {
		this._player = _player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.panillaLogger = panillaLogger;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			packetInspector.checkPlayIn(_player, msg);
		} catch (PacketException e) {
			containerCleaner.clean(_player);
			panillaLogger.warn(_player, e);

			return; // drop the packet
		}

		super.channelRead(ctx, msg);
	}

}
