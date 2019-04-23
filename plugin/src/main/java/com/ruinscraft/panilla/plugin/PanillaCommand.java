package com.ruinscraft.panilla.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PanillaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final String version = PanillaPlugin.get().getDescription().getVersion();
        final List<String> authors = PanillaPlugin.get().getDescription().getAuthors();

        sender.sendMessage(ChatColor.GOLD + "Running Panilla version: " + version + " by " + String.join(", ", authors));

        return true;
    }

}
