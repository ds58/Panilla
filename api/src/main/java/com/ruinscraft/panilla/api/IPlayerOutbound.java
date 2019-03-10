package com.ruinscraft.panilla.api;

import org.bukkit.entity.Player;

import io.netty.channel.ChannelOutboundHandler;

/*
 * Intercepts packets going to the player from the server
 */
public interface IPlayerOutbound extends ChannelOutboundHandler {

	Player getBukkitPlayer();
	
}
