package com.ruinscraft.panilla.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PanillaPlugin.get().getPlayerInjector().register(
                PanillaPlugin.get(),
                new BukkitPanillaPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PanillaPlugin.get().getPlayerInjector().unregister(
                new BukkitPanillaPlayer(event.getPlayer()));
    }

}
