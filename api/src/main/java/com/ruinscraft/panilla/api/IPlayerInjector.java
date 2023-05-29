package com.ruinscraft.panilla.api;

import org.bukkit.entity.Player;

public interface IPlayerInjector {

	public static final String MINECRAFT_PACKET_HANDLER = "packet_handler";
	public static final String CHANNEL_IN = "panilla_in";
	public static final String CHANNEL_OUT = "panilla_out";

	void register(Player bukkitPlayer);

	void unregister(Player bukkitPlayer);

}
