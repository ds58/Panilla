package com.ruinscraft.panilla.plugin;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;

public class PanillaPlugin extends JavaPlugin {

	private PConfig panillaConfig;
	private IProtocolConstants protocolConstants;
	private IContainerCleaner containerCleaner;
	private IPacketInspector packetInspector;
	private IPlayerInjector playerInjector;

	public PConfig getPanillaConfig() {
		return panillaConfig;
	}

	public IProtocolConstants getProtocolConstants() {
		return protocolConstants;
	}

	public IContainerCleaner getContainerCleaner() {
		return containerCleaner;
	}

	public IPacketInspector getPacketInspector() {
		return packetInspector;
	}

	public IPlayerInjector getPlayerInjector() {
		return playerInjector;
	}

	private synchronized void loadConfig() {
		saveDefaultConfig();

		panillaConfig = new PConfig();

		panillaConfig.localeFile = getConfig().getString("locale-file");
		panillaConfig.consoleLogging = getConfig().getBoolean("logging.console");
		panillaConfig.chatLogging = getConfig().getBoolean("logging.chat");
		panillaConfig.strictness = PStrictness.valueOf(getConfig().getString("strictness").toUpperCase());
		panillaConfig.nbtWhitelist = getConfig().getStringList("nbt-whitelist");
	}

	private String v_Version() {
		return getServer().getClass().getPackage().getName().substring("org.bukkit.craftbukkit.".length());
	}

	@Override
	public void onEnable() {
		singleton = this;

		loadConfig();

		PanillaLogger panillaLogger = new PanillaLogger(this);

		try {
			panillaLogger.loadLocale(panillaConfig.localeFile);
		} catch (IOException e) {
			getLogger().severe(e.getMessage());
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		final String v_Version = v_Version();

		switch (v_Version) {
		case "v1_12_R1":
			protocolConstants = new com.ruinscraft.panilla.v1_12_R1.ProtocolConstants();
			containerCleaner = new com.ruinscraft.panilla.v1_12_R1.ContainerCleaner(panillaConfig,
					protocolConstants);
			packetInspector = new com.ruinscraft.panilla.v1_12_R1.io.PacketInspector(panillaConfig,
					protocolConstants);
			playerInjector = new com.ruinscraft.panilla.v1_12_R1.io.PlayerInjector(packetInspector, containerCleaner,
					protocolConstants, panillaConfig, panillaLogger);
			break;
		case "v1_13_R2":
			protocolConstants = new com.ruinscraft.panilla.v1_13_R2.ProtocolConstants();
			containerCleaner = new com.ruinscraft.panilla.v1_13_R2.ContainerCleaner(panillaConfig,
					protocolConstants);
			packetInspector = new com.ruinscraft.panilla.v1_13_R2.io.PacketInspector(panillaConfig,
					protocolConstants);
			playerInjector = new com.ruinscraft.panilla.v1_13_R2.io.PlayerInjector(packetInspector, containerCleaner,
					protocolConstants, panillaConfig, panillaLogger);
			break;
		default:
			getLogger().severe("Minecraft version " + v_Version + " is not supported.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		/* Register listeners */
		getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);

		/* Register commands */
		getCommand("panilla").setExecutor(new PanillaCommand());

		for (Player player : Bukkit.getOnlinePlayers()) {
			playerInjector.register(player);
		}
	}

	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerInjector.unregister(player);
		}

		singleton = null;
	}

	/* singleton ========================================== */
	private static PanillaPlugin singleton;

	public static PanillaPlugin get() {
		return singleton;
	}
	/* singleton ========================================== */

}
