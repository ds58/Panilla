package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IPacketInspector;
import com.ruinscraft.panilla.api.IPlayerOutbound;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter implements IPlayerOutbound {

	private final IPacketInspector packetInspector;
	private final Player bukkitPlayer;
	
	public PlayerOutbound(IPacketInspector packetInspector, Player bukkitPlayer) {
		this.packetInspector = packetInspector;
		this.bukkitPlayer = bukkitPlayer;
	}
	
	@Override
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			packetInspector.checkSize(msg);
		} catch (OversizedPacketException e) {
			e.printStackTrace();
			
			return;
		}
		
		super.write(ctx, msg, promise);
	}
	
}
