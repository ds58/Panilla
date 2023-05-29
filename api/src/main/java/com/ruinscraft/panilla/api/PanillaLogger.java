package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OversizedPacketException;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.exception.UndersizedPacketException;

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

        if (e instanceof UndersizedPacketException) {
            UndersizedPacketException undersizedPacketException = (UndersizedPacketException) e;

            message += " " + String.format(
                    panilla.getPanillaLocale().getTranslation("packet-dropped-reason-too-small"),
                    undersizedPacketException.getSizeBytes(),
                    panilla.getProtocolConstants().packetCompressionLevel());
        }

        else if (e instanceof OversizedPacketException) {
            OversizedPacketException oversizedPacketException = (OversizedPacketException) e;

            message += " " + String.format(
                    panilla.getPanillaLocale().getTranslation("packet-dropped-reason-too-large"),
                    oversizedPacketException.getSizeBytes(),
                    panilla.getProtocolConstants().maxPacketSizeBytes());
        }

        else if (e instanceof NbtNotPermittedException) {
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
