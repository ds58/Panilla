package com.ruinscraft.panilla.v1_12_R1.io;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import com.ruinscraft.panilla.api.io.PlayerInbound;
import com.ruinscraft.panilla.api.io.PlayerOutbound;

import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PlayerInjector implements IPlayerInjector {

	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final PanillaLogger panillaLogger;

	public PlayerInjector(IPacketInspector packetInspector, IContainerCleaner containerCleaner,
			PanillaLogger panillaLogger) {
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.panillaLogger = panillaLogger;
	}

	private static Channel getPlayerChannel(Player _player) throws IllegalArgumentException {
		if (!(_player instanceof CraftPlayer)) {
			throw new IllegalArgumentException("_player not instanceof CraftPlayer");
		}

		CraftPlayer craftPlayer = (CraftPlayer) _player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.playerConnection.networkManager.channel;
	}

	@Override
	public void register(final Object _player) {
		if (!(_player instanceof Player)) {
			return;
		}

		Player bukkitPlayer = (Player) _player;
		Channel channel = getPlayerChannel(bukkitPlayer);

		/* Register inbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_IN) == null) {
			PlayerInbound inbound = new PlayerInbound(bukkitPlayer, packetInspector, containerCleaner, panillaLogger);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_IN, inbound);
		}

		/* Register outbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_OUT) == null) {
			PlayerOutbound outbound = new PlayerOutbound(bukkitPlayer, packetInspector, containerCleaner,
					panillaLogger);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_OUT, outbound);
		}
	}

	@Override
	public void unregister(final Object _player) {
		if (!(_player instanceof Player)) {
			return;
		}

		Player bukkitPlayer = (Player) _player;
		Channel channel = getPlayerChannel(bukkitPlayer);

		/* Unregister inbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_IN) != null) {
			channel.pipeline().remove(PANILLA_CHANNEL_IN);
		}

		/* Unregister outbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_OUT) != null) {
			channel.pipeline().remove(PANILLA_CHANNEL_OUT);
		}
	}

}
