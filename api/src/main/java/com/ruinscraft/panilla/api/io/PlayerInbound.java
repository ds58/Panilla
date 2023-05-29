package com.ruinscraft.panilla.api.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final Object player;
	private final IPacketInspector packetInspector;

	public PlayerInbound(Object player, IPacketInspector packetInspector) {
		this.player = player;
		this.packetInspector = packetInspector;
	}
	
	public Object getPlayer() {
		return player;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			packetInspector.checkIn(player, msg);
			
			super.channelRead(ctx, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
