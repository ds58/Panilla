package com.ruinscraft.panilla.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PanillaPlugin.get().getPlayerInjector().register(new BukkitPanillaPlayer(player), PanillaPlugin.get());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PanillaPlugin.get().getPlayerInjector().unregister(new BukkitPanillaPlayer(player));
    }

}
