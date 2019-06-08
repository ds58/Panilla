package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.PacketException;

public class PanillaLogger {

    private static final String CHAT_PERMISSION = "panilla.log.chat";

    private final IPanilla panilla;

    public PanillaLogger(IPanilla panilla) {
        this.panilla = panilla;
    }

    public void warn(IPanillaPlayer player, PacketException e) {
        if (panilla.getPanillaLocale() == null) {
            panilla.getPlatform().getLogger().warning("Locale is not loaded");
            return;
        }

        String message = panilla.getPanillaLocale().getTranslation("prefix");

        if (e.isFrom()) {
            message += String.format(
                    panilla.getPanillaLocale().getTranslation("packet-from-dropped"),
                    player.getName(),
                    e.getNmsClass());
        } else {
            message += String.format(
                    panilla.getPanillaLocale().getTranslation("packet-to-dropped"),
                    player.getName(),
                    e.getNmsClass());
        }

        if (e instanceof NbtNotPermittedException) {
            NbtNotPermittedException nbtNotPermittedException = (NbtNotPermittedException) e;

            message += " " + String.format(
                    panilla.getPanillaLocale().getTranslation("packet-dropped-reason-invalid-nbt"),
                    nbtNotPermittedException.getTagName());
        }

        message = panilla.getPlatform().translateColorCodes(message);

        if (panilla.getPanillaConfig().chatLogging) {
            for (IPanillaPlayer onlinePlayer : panilla.getPlatform().getOnlinePlayers()) {
                if (onlinePlayer.hasPermission(CHAT_PERMISSION)) {
                    onlinePlayer.sendMessage(message);
                }
            }
        }

        if (panilla.getPanillaConfig().consoleLogging) {
            panilla.getPlatform().getLogger().info(message);
        }
    }

}
