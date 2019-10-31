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

    private final PanillaPlugin panillaPlugin;

    public PanillaCommand(PanillaPlugin panillaPlugin) {
        this.panillaPlugin = panillaPlugin;
    }

    private boolean showInfo(CommandSender sender) {
        String version = panillaPlugin.getDescription().getVersion();
        List<String> authors = panillaPlugin.getDescription().getAuthors();
        sender.sendMessage(ChatColor.GOLD + "Running Panilla version: " + version + " by " + String.join(", ", authors));
        return true;
    }

    private boolean showDebug(CommandSender sender) {
        List<String> debugInfo = new ArrayList<>();
        debugInfo.add(ChatColor.BOLD + "=== Panilla Debug ===");
        debugInfo.add("Bukkit version: " + Bukkit.getVersion());
        debugInfo.add("Panilla version: " + panillaPlugin.getDescription().getVersion());
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return showInfo(sender);
        } else if (args[0].equalsIgnoreCase("debug")) {
            return showDebug(sender);
        }

        return true;
    }

}
