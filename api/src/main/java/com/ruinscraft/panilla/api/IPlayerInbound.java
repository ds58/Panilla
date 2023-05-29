package com.ruinscraft.panilla.api;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelInboundHandler;

/*
 * Intercepts packets coming from a player to the server
 */
public interface IPlayerInbound extends ChannelInboundHandler {

	Player getBukkitPlayer();
	
}
