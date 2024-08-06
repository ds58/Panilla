package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class JoinQuitListener implements Listener {

    private final IPanilla panilla;
    private JavaPlugin panillaPlugin;

    public JoinQuitListener(JavaPlugin panillaPlugin, IPanilla panilla) {
        this.panillaPlugin = panillaPlugin;
        this.panilla = panilla;
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());
        panilla.getInventoryCleaner().clean(pplayer);
        try {
            panilla.getPlayerInjector().register(panilla, pplayer);
        } catch (IOException e) {
            // Ignore
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());
        try {
            panilla.getPlayerInjector().unregister(pplayer);
        } catch (IOException e) {
            // Ignore
        }
    }

}
