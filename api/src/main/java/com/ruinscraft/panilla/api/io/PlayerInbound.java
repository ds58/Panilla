package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final Object _player;
	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;

	public PlayerInbound(Object _player, IPacketInspector packetInspector, IContainerCleaner containerCleaner) {
		this._player = _player;
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			packetInspector.checkPlayIn(_player, msg);
		} catch (OversizedPacketException e) {
			e.printStackTrace();
		} catch (NbtNotPermittedException e) {
			containerCleaner.clean(_player);
		} catch (SignLineLengthTooLongException e) {
			e.printStackTrace();
		}
		
		super.channelRead(ctx, msg);
	}

}
