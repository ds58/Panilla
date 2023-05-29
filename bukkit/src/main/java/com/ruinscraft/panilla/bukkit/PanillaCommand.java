package com.ruinscraft.panilla.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PanillaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "debug":
                        if (sender.hasPermission("panilla.command.debug")) {
                            return showDebug(sender);
                        }
                }
            default:
                return showInfo(sender);
        }
    }

    private static boolean showInfo(CommandSender sender) {
        final String version = PanillaPlugin.get().getDescription().getVersion();
        final List<String> authors = PanillaPlugin.get().getDescription().getAuthors();
        sender.sendMessage(ChatColor.GOLD + "Running Panilla version: " + version + " by " + String.join(", ", authors));
        return true;
    }

    private static boolean showDebug(CommandSender sender) {
        List<String> debugInfo = new ArrayList<>();
        debugInfo.add(ChatColor.BOLD + "=== Panilla Debug ===");
        debugInfo.add("Bukkit version: " + Bukkit.getVersion());
        debugInfo.add("Panilla version: " + PanillaPlugin.get().getDescription().getVersion());
        debugInfo.add("Java version: " + System.getProperty("java.version"));
        List<String> pluginNames = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            pluginNames.add(plugin.getName());
        }
        debugInfo.add("Plugins: [" + String.join(", ", pluginNames) + "]");
        for (String line : debugInfo) {
            sender.sendMessage(line);
        }
        return true;
    }

}
