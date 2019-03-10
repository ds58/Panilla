package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import com.ruinscraft.panilla.api.io.PlayerInbound;
import com.ruinscraft.panilla.api.io.PlayerOutbound;

import io.netty.channel.Channel;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PlayerInjector implements IPlayerInjector {

	private final IPacketInspector packetInspector;

	public PlayerInjector(IPacketInspector packetInspector) {
		this.packetInspector = packetInspector;
	}

	private static Channel getPlayerChannel(Player bukkitPlayer) throws IllegalArgumentException {
		if (!(bukkitPlayer instanceof CraftPlayer)) {
			throw new IllegalArgumentException("bukkitPlayer not instanceof CraftPlayer");
		}

		CraftPlayer craftPlayer = (CraftPlayer) bukkitPlayer;
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		return entityPlayer.playerConnection.networkManager.channel;
	}

	@Override
	public void register(final Object object) {
		if (!(object instanceof Player)) {
			return;
		}

		Player bukkitPlayer = (Player) object;
		Channel channel = getPlayerChannel(bukkitPlayer);

		/* Register inbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_IN) == null) {
			PlayerInbound inbound = new PlayerInbound(packetInspector);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_IN, inbound);
		}

		/* Register outbound */
		if (channel.pipeline().get(PANILLA_CHANNEL_OUT) == null) {
			PlayerOutbound outbound = new PlayerOutbound(packetInspector);
			channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_OUT, outbound);
		}
	}

	@Override
	public void unregister(final Object object) {
		if (!(object instanceof Player)) {
			return;
		}

		Player bukkitPlayer = (Player) object;
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
