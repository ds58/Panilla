package com.ruinscraft.panilla.api.io;

import org.bukkit.entity.Player;

public interface IPlayerInjector {

    String MINECRAFT_CHANNEL_DPLX = "packet_handler";
    String PANILLA_CHANNEL_IN = "panilla_in";
    String PANILLA_CHANNEL_OUT = "panilla_out";
    String BYPASS_PERMISSION = "panilla.bypass";

    static boolean canBypass(Player player) {
        return player.isOp() || player.hasPermission(BYPASS_PERMISSION);
    }

    void register(final Player player);

    void unregister(final Player player);

}
