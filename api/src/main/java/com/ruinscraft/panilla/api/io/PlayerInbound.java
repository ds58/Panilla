package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final Object _player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final IPanillaLogger panillaLogger;

	public PlayerInbound(Object _player,
			IPacketInspector packetInspector,
			IContainerCleaner containerCleaner,
			IPanillaLogger panillaLogger) {
		this._player = _player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.panillaLogger = panillaLogger;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			packetInspector.checkPlayIn(_player, msg);
		} catch (OversizedPacketException | NbtNotPermittedException | SignLineLengthTooLongException e) {
			containerCleaner.clean(_player);

			panillaLogger.all(panillaLogger.generateWarning(_player, e));
			
			return; // drop the packet
		}

		super.channelRead(ctx, msg);
	}

}
