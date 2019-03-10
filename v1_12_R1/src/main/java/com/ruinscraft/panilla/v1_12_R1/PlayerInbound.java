package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IPacketInspector;
import com.ruinscraft.panilla.api.IPlayerInbound;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerInbound extends ChannelInboundHandlerAdapter implements IPlayerInbound {
	
	private final IPacketInspector packetInspector;
	private final Player bukkitPlayer;

	public PlayerInbound(IPacketInspector packetInspector, Player bukkitPlayer) {
		this.packetInspector = packetInspector;
		this.bukkitPlayer = bukkitPlayer;
	}

	@Override
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			packetInspector.checkSize(msg);
		} catch (OversizedPacketException e) {
			e.printStackTrace();
			
			return;
		}
		
		super.channelRead(ctx, msg);
	}

}
