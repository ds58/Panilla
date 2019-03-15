package com.ruinscraft.panilla.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PanillaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		final String version = PanillaPlugin.get().getDescription().getVersion();

		sender.sendMessage("Running Panilla version: " + version);

		return true;
	}

}
