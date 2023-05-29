package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IPlayerOutbound;

import io.netty.channel.ChannelOutboundHandlerAdapter;

public class PlayerOutbound extends ChannelOutboundHandlerAdapter implements IPlayerOutbound {

	private Player bukkitPlayer;
	
	public PlayerOutbound(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}
	
	@Override
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	// do nothing for now...
	
}
