package com.ruinscraft.panilla.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.panilla.api.IPlayerInjector;
import com.ruinscraft.panilla.api.exception.UnsupportedMinecraftVersionException;

public class PanillaPlugin extends JavaPlugin {

	private IPlayerInjector playerInjector;

	public IPlayerInjector getPlayerInjector() {
		return playerInjector;
	}
	
	@Override
	public void onEnable() {
		singleton = this;
		
		final String v_Version = 
				getServer().
				getClass().
				getPackage().
				getName().
				substring("org.bukkit.craftbukkit.".length());

		try {
			playerInjector = getPlayerInjector(v_Version);
		} catch (UnsupportedMinecraftVersionException e) {
			disableAndWarn(e.getMessage());
			
			return;
		}
		
		/* Register listeners */
		getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
	}

	@Override
	public void onDisable() {
		singleton = null;
	}
	
	private void disableAndWarn(String reason) {
		getLogger().severe(reason);
		getServer().getPluginManager().disablePlugin(this);
	}
	
	/* ========================================== static ========================================== */
	private static PanillaPlugin singleton;
	
	public static PanillaPlugin get() {
		return singleton;
	}
	
	private static IPlayerInjector getPlayerInjector(String v_Version) throws UnsupportedMinecraftVersionException {
		switch (v_Version) {
		case "v1_12_R1":
			return new com.ruinscraft.panilla.v1_12_R1.PlayerInjector();
		case "v1_13_R2":
			return null;	// TODO: impl 1.13.2
		default:
			throw new UnsupportedMinecraftVersionException(v_Version);
		}
	}
	/* ========================================== static ========================================== */

}
