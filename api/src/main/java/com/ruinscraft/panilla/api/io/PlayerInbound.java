package com.ruinscraft.panilla.api.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter {

	private final IPacketInspector packetInspector;

	public PlayerInbound(IPacketInspector packetInspector) {
		this.packetInspector = packetInspector;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			packetInspector.checkIn(msg);
			
			super.channelRead(ctx, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
