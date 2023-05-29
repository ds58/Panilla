package com.ruinscraft.panilla.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;

public class PanillaPlugin extends JavaPlugin {

	private PConfig panillaConfig;
	private IPanillaLogger panillaLogger;
	private IProtocolConstants protocolConstants;
	private INbtChecker nbtChecker;
	private IContainerCleaner containerCleaner;
	private IPacketInspector packetInspector;
	private IPlayerInjector playerInjector;

	public PConfig getPanillaConfig() {
		return panillaConfig;
	}
	
	public IPanillaLogger getPanillaLogger() {
		return panillaLogger;
	}

	public IProtocolConstants getProtocolConstants() {
		return protocolConstants;
	}

	public INbtChecker getNbtChecker() {
		return nbtChecker;
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

		panillaConfig.consoleLogging = getConfig().getBoolean("logging.console");
		panillaConfig.chatLogging = getConfig().getBoolean("logging.chat");
		panillaConfig.strictness = PStrictness.valueOf(getConfig().getString("strictness").toUpperCase());
		panillaConfig.nbtWhitelist = getConfig().getStringList("nbt-whitelist");
	}

	private String v_Version() {
		return 	getServer().
				getClass().
				getPackage().
				getName().
				substring("org.bukkit.craftbukkit.".length());
	}

	@Override
	public void onEnable() {
		singleton = this;

		loadConfig();
		
		panillaLogger = new PanillaLogger();

		final String v_Version = v_Version();

		switch (v_Version) {
		case "v1_12_R1":
			protocolConstants = new com.ruinscraft.panilla.v1_12_R1.ProtocolConstants();
			nbtChecker = new com.ruinscraft.panilla.v1_12_R1.NbtChecker(protocolConstants);
			containerCleaner = new com.ruinscraft.panilla.v1_12_R1.ContainerCleaner(panillaConfig.strictness, nbtChecker);
			packetInspector = new com.ruinscraft.panilla.v1_12_R1.io.PacketInspector(panillaConfig.strictness, protocolConstants, nbtChecker);
			playerInjector = new com.ruinscraft.panilla.v1_12_R1.io.PlayerInjector(packetInspector, containerCleaner, panillaLogger);
			break;
		case "v1_13_R2":
			// TODO: impl 1.13.2
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

	/* ========================================== singleton ========================================== */
	private static PanillaPlugin singleton;

	public static PanillaPlugin get() {
		return singleton;
	}
	/* ========================================== singleton ========================================== */

}
