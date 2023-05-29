package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.config.PConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class BukkitPanillaLogger implements IPanillaLogger {

    private final IPanilla panilla;
    private final Logger jLogger;

    public BukkitPanillaLogger(IPanilla panilla, Logger jLogger) {
        this.panilla = panilla;
        this.jLogger = jLogger;
    }

    private static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void log(String message, boolean colorize) {
        if (colorize) {
            message = colorize(message);
        }

        if (panilla.getPConfig().consoleLogging) {
            jLogger.info(message);
        }

        if (panilla.getPConfig().chatLogging) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(PConfig.PERMISSION_LOG_CHAT)) {
                    player.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void info(String message, boolean colorize) {
        if (colorize) {
            message = colorize(message);
        }

        jLogger.info(message);
    }

    @Override
    public void warning(String message, boolean colorize) {
        if (colorize) {
            message = colorize(message);
        }

        jLogger.warning(message);
    }

}
