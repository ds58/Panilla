package com.ruinscraft.panilla.api.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter {

	private final Object player;
	private final IPacketInspector packetInspector;

	public PlayerOutbound(Object player, IPacketInspector packetInspector) {
		this.player = player;
		this.packetInspector = packetInspector;
	}

	public Object getPlayer() {
		return player;
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			packetInspector.checkOut(player, msg);
			
			super.write(ctx, msg, promise);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
