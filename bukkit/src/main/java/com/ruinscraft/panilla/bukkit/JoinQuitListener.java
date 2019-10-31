package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final IPanilla panilla;

    public JoinQuitListener(IPanilla panilla) {
        this.panilla = panilla;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());
        panilla.getPlayerInjector().register(panilla, pplayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());
        panilla.getPlayerInjector().unregister(pplayer);
    }

}
