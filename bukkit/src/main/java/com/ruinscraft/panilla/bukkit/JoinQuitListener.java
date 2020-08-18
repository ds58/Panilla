package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinQuitListener implements Listener {

    private JavaPlugin panillaPlugin;
    private final IPanilla panilla;

    public JoinQuitListener(JavaPlugin panillaPlugin, IPanilla panilla) {
        this.panillaPlugin = panillaPlugin;
        this.panilla = panilla;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());

        panillaPlugin.getServer().getScheduler().runTaskLater(panillaPlugin, () -> {
            panilla.getPlayerInjector().register(panilla, pplayer);
        }, 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        IPanillaPlayer pplayer = new BukkitPanillaPlayer(event.getPlayer());
        panilla.getPlayerInjector().unregister(pplayer);
    }

}
