package com.ruinscraft.panilla.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.panilla.api.PacketHandler;
import com.ruinscraft.panilla.api.UnsupportedMinecraftVersionException;

public class PanillaPlugin extends JavaPlugin {

	private PacketHandler packetHandler;

	public PacketHandler getPacketHandler() {
		return packetHandler;
	}
	
	@Override
	public void onEnable() {
		final String v_Version = 
				getServer().
				getClass().
				getPackage().
				getName().
				substring("net.minecraft.server.".length() - 1);

		try {
			packetHandler = getPacketHandler(v_Version);
		} catch (UnsupportedMinecraftVersionException e) {
			disable(e.getMessage());
			
			return;
		}
		
		
	}

	@Override
	public void onDisable() {

	}
	
	private void disable(String reason) {
		getLogger().severe(reason);
		getServer().getPluginManager().disablePlugin(this);
	}
	
	/* ========================================== static ========================================== */
	private static PanillaPlugin singleton;
	
	public static PanillaPlugin get() {
		return singleton;
	}
	
	private static PacketHandler getPacketHandler(String v_Version) throws UnsupportedMinecraftVersionException {
		switch (v_Version) {
		case "v1_12_R1":
			return new com.ruinscraft.panilla.v1_12_R1.PacketHandler();
		case "v1_13_R2":
			return new com.ruinscraft.panilla.v1_13_R2.PacketHandler();
		default:
			throw new UnsupportedMinecraftVersionException(v_Version);
		}
	}
	/* ========================================== static ========================================== */

}
