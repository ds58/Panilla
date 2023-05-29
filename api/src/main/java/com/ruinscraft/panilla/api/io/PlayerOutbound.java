package com.ruinscraft.panilla.api.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter {

	private final IPacketInspector packetInspector;

	public PlayerOutbound(IPacketInspector packetInspector) {
		this.packetInspector = packetInspector;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			packetInspector.checkOut(msg);
			
			super.write(ctx, msg, promise);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
