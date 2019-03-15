package com.ruinscraft.panilla.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.exception.SignLineLengthTooLongException;

public class PanillaLogger {

	private static final String CHAT_PERMISSION = "panilla.log.chat";

	private final Plugin plugin;
	private final PConfig config;
	private final IProtocolConstants protocolConstants;
	private FileConfiguration locale;

	public PanillaLogger(Plugin plugin, PConfig config, IProtocolConstants protocolConstants) {
		this.plugin = plugin;
		this.config = config;
		this.protocolConstants = protocolConstants;
	}

	public void loadLocale(String localeFileName) throws IOException {
		File file = new File(plugin.getDataFolder(), localeFileName);

		if (!file.exists()) {
			file.getParentFile().mkdirs();

			if (plugin.getResource(localeFileName) == null) {
				throw new IOException("Could not locate locale file: " + localeFileName);
			}

			plugin.saveResource(localeFileName, false);
		}

		locale = new YamlConfiguration();

		try {
			locale.load(file);
		} catch (InvalidConfigurationException e) {
			throw new IOException(e.getMessage());
		}
	}

	public void warn(Object _player, PacketException e) {
		if (locale == null) {
			plugin.getLogger().warning("Locale is not loaded");
			return;
		}

		if (!(_player instanceof Player)) {
			return;
		}

		Player player = (Player) _player;

		String message = locale.getString("prefix");

		if (e.isFrom()) {
			message += String.format(locale.getString("packet-from-dropped"),
					player.getName(),
					e.getNmsClass());
		} else {
			message += String.format(locale.getString("packet-to-dropped"),
					player.getName(),
					e.getNmsClass());
		}

		if (e instanceof OversizedPacketException) {
			OversizedPacketException oversizedPacketException = (OversizedPacketException) e;

			message += " " + String.format(locale.getString("packet-dropped-reason-too-large"),
					oversizedPacketException.getSizeBytes(),
					protocolConstants.maxPacketSizeBytes());
		}

		else if (e instanceof NbtNotPermittedException) {
			NbtNotPermittedException nbtNotPermittedException = (NbtNotPermittedException) e;

			message += " " + String.format(locale.getString("packet-dropped-reason-invalid-nbt"),
					nbtNotPermittedException.getTagName());
		}

		else if (e instanceof SignLineLengthTooLongException) {
			message += " " + locale.getString("packet-dropped-reason-sign-length");
		}

		message = ChatColor.translateAlternateColorCodes('&', message);

		if (config.chatLogging) {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (onlinePlayer.hasPermission(CHAT_PERMISSION)) {
					onlinePlayer.sendMessage(message);
				}
			}
		}

		if (config.consoleLogging) {
			plugin.getLogger().info(message);
		}
	}

}
