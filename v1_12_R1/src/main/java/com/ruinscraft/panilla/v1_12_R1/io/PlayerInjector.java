package com.ruinscraft.panilla.v1_12_R1.io;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import com.ruinscraft.panilla.api.io.PlayerInbound;
import com.ruinscraft.panilla.api.io.PlayerOutbound;

import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PlayerInjector implements IPlayerInjector {

	private final IPacketInspector packetInspector;
	private final IContainerCleaner containerCleaner;
	private final IProtocolConstants protocolConstants;
	private final PConfig config;
	private final PanillaLogger panillaLogger;

	public PlayerInjector(IPacketInspector packetInspector, IContainerCleaner containerCleaner,
			IProtocolConstants protocolConstants, PConfig config, PanillaLogger panillaLogger) {
		this.packetInspector = packetInspector;
		this.containerCleaner = containerCleaner;
		this.protocolConstants = protocolConstants;
		this.config = config;
		this.panillaLogger = panillaLogger;
	}

	private static Channel getPlayerChannel(Player player) throws IllegalArgumentException {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.playerConnection.networkManager.channel;
	}

	@Override
	public void register(final Player player) {
		Channel channel = getPlayerChannel(player);

		/* Register inbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_IN) == null) {
			PlayerInbound inbound = new PlayerInbound(player, packetInspector, containerCleaner,
					protocolConstants, config, panillaLogger);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_IN, inbound);
		}

		/* Register outbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_OUT) == null) {
			PlayerOutbound outbound = new PlayerOutbound(player, packetInspector, containerCleaner,
					protocolConstants, config, panillaLogger);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_OUT, outbound);
		}
	}

	@Override
	public void unregister(final Player player) {
		Channel channel = getPlayerChannel(player);

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
