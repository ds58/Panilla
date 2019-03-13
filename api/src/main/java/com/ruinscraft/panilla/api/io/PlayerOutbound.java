package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter {

	private final Object _player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final IPanillaLogger panillaLogger;

	public PlayerOutbound(Object _player,
			IPacketInspector packetInspector,
			IContainerCleaner containerCleaner,
			IPanillaLogger panillaLogger) {
		this._player = _player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.panillaLogger = panillaLogger;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			packetInspector.checkPlayOut(msg);
		} catch (OversizedPacketException | NbtNotPermittedException e) {
			containerCleaner.clean(_player);
			
			panillaLogger.all(panillaLogger.generateWarning(_player, e));
			
			return; // drop the packet
		}
		
		super.write(ctx, msg, promise);
	}

}
