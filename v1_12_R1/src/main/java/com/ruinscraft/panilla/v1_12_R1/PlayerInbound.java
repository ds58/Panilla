package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IPlayerInbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter implements IPlayerInbound {

	private Player bukkitPlayer;
	
	public PlayerInbound(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}
	
	@Override
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg.getClass().getName());
		super.channelRead(ctx, msg);
	}

}
