package com.ruinscraft.panilla.api.io;

import org.bukkit.entity.Player;

public interface IPlayerInjector {

	static String MINECRAFT_CHANNEL_DPLX = "packet_handler";
	static String PANILLA_CHANNEL_IN = "panilla_in";
	static String PANILLA_CHANNEL_OUT = "panilla_out";

	void register(final Player player);

	void unregister(final Player player);

}
