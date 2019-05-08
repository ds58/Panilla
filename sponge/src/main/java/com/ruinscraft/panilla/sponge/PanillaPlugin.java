package com.ruinscraft.panilla.sponge;

import com.google.inject.Inject;
import com.ruinscraft.panilla.api.*;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PLocale;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Color;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

@Plugin(id = "panilla", name = "Panilla")
public class PanillaPlugin implements IPanilla, IPanillaPlatform {

    @Inject
    private Game game;
    @Inject
    private Logger logger;

    private PConfig pConfig;
    private PLocale pLocale;
    private IProtocolConstants protocolConstants;
    private IPlayerInjector playerInjector;
    private IPacketInspector packetInspector;
    private IContainerCleaner containerCleaner;
    private IEnchantments enchantments;

    @Override
    public PConfig getPanillaConfig() {
        return pConfig;
    }

    @Override
    public PLocale getPanillaLocale() {
        return pLocale;
    }

    @Override
    public IPanillaPlatform getPlatform() {
        return this;
    }

    @Override
    public IProtocolConstants getProtocolConstants() {
        return protocolConstants;
    }

    @Override
    public IPlayerInjector getPlayerInjector() {
        return playerInjector;
    }

    @Override
    public IPacketInspector getPacketInspector() {
        return packetInspector;
    }

    @Override
    public IContainerCleaner getContainerCleaner() {
        return containerCleaner;
    }

    @Override
    public IEnchantments getEnchantments() {
        return enchantments;
    }

    @Override
    public String translateColorCodes(String string) {
        return TextSerializers.formattingCode('&').replaceCodes(string, TextSerializers.LEGACY_FORMATTING_CODE);
    }

    @Override
    public boolean isValidPotionColor(int bgr) {
        int r = (bgr >> 0) & 0xFF;
        int g = (bgr >> 8) & 0xFF;
        int b = (bgr >> 16) & 0xFF;
        int rgb = (r << 16) | (g << 8) | (b << 0);

        Color.ofRgb(rgb);   // TODO:

        return true;
    }

    @Override
    public Collection<IPanillaPlayer> getOnlinePlayers() {
        Collection<IPanillaPlayer> panillaPlayers = Collections.EMPTY_SET;

        for (Player player : game.getServer().getOnlinePlayers()) {

        }

        return panillaPlayers;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        

    }

}
