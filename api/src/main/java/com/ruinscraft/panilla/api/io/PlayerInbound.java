package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IContainerCleaner;

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
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			packetInspector.checkIn(_player, msg);
			super.channelRead(ctx, msg);
		} catch (Exception e) {
			containerCleaner.clean(_player);
		}
	}

}
