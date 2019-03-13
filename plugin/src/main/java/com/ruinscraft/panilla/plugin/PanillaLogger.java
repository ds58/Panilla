package com.ruinscraft.panilla.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IPanillaLogger;

public class PanillaLogger implements IPanillaLogger {

	private static final String CHAT_PERMISSION = "panilla.log.chat";
	
	@Override
	public void all(String message) {
		chat(message);
		console(message);
	}
	
	@Override
	public void chat(String message) {
		if (!PanillaPlugin.get().getPanillaConfig().chatLogging) return;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(CHAT_PERMISSION)) {
				player.sendMessage(message);
			}
		}
	}

	@Override
	public void console(String message) {
		if (!PanillaPlugin.get().getPanillaConfig().consoleLogging) return;
		
		PanillaPlugin.get().getLogger().info(message);
	}

	@Override
	public String generateWarning(Object _player, Exception exception) {
		if (_player instanceof Player) {
			Player player = (Player) _player;

			return String.format("Cancelled packet on %s: %s", player.getName(), exception.getMessage());
		}

		return String.format("Cancelled packet on ?: %s", exception.getMessage());
	}
	
}
