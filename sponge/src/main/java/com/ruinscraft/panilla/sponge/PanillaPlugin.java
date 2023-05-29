package com.ruinscraft.panilla.sponge;

import com.google.inject.Inject;
import com.ruinscraft.panilla.api.*;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PLocale;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Color;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

@Plugin(id = "panilla", name = "Panilla")
public class PanillaPlugin implements IPanilla, IPanillaPlatform {

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
    public String getVersionString() {
        return Sponge.getPluginManager().getPlugin("panilla").get().getVersion().get();
    }

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
        Sponge.getGame().getServer().getOnlinePlayers().forEach(p -> panillaPlayers.add(new SpongePanillaPlayer(p)));
        return panillaPlayers;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        CommandSpec panillaCommandSpec = CommandSpec.builder()
                .description(Text.of("Panilla information"))
                .executor(new PanillaCommand())
                .build();

        Sponge.getCommandManager().register(this, panillaCommandSpec);
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        for (Player player : Sponge.getGame().getServer().getOnlinePlayers()) {
            playerInjector.unregister(new SpongePanillaPlayer(player));
        }
    }

    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        playerInjector.register(new SpongePanillaPlayer(player));
    }

    @Listener
    public void onClientConnectionDisconnect(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        playerInjector.unregister(new SpongePanillaPlayer(player));
    }

}
