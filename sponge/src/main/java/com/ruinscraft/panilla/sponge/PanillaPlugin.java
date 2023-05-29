package com.ruinscraft.panilla.sponge;

import com.google.inject.Inject;
import com.ruinscraft.panilla.api.*;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPacketSerializer;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import com.ruinscraft.panilla.forge112.InventoryCleaner;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.logging.Logger;

@Plugin(id = "panilla", name = "Panilla")
public class PanillaPlugin implements IPanilla {

    /* Sponge dependency injection */
    @Inject
    private Logger logger;

    private PConfig pConfig;
    private PTranslations pTranslations;
    private IPanillaLogger panillaLogger;
    private IProtocolConstants protocolConstants;
    private IPlayerInjector playerInjector;
    private IPacketInspector packetInspector;
    private IInventoryCleaner containerCleaner;
    private IEnchantments enchantments;

    @Override
    public PConfig getPConfig() {
        return pConfig;
    }

    @Override
    public PTranslations getPTranslations() {
        return pTranslations;
    }

    @Override
    public IPanillaLogger getPanillaLogger() {
        return panillaLogger;
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
    public IInventoryCleaner getInventoryCleaner() {
        return containerCleaner;
    }

    @Override
    public IEnchantments getEnchantments() {
        return enchantments;
    }

    @Override
    public IPacketSerializer createPacketSerializer(Object byteBuf) {
        return null;    // TODO: implement
    }

    @Override
    public void exec(Runnable runnable) {
        // TODO:
    }

    private synchronized void loadConfig() {
        // TODO: how do config files work on Sponge?
    }

    private synchronized void loadTranslations(String languageKey) {
        try {
            pTranslations = PTranslations.get(languageKey);
        } catch (IOException e) {
            getPanillaLogger().warning("Could not load language translations for " + languageKey, false);
        }
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        loadConfig();
        loadTranslations(pConfig.language);

        panillaLogger = new SpongePanillaLogger(logger);
        enchantments = new SpongeEnchantments();

        MinecraftVersion minecraftVersion = Sponge.getGame().getPlatform().getMinecraftVersion();

        // TODO: no idea if this works
        switch (minecraftVersion.getName()) {
            case "1.12":
            case "1.12.1":
            case "1.12.2":
                protocolConstants = new DefaultProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.forge112.io.PlayerInjector();
                packetInspector = new com.ruinscraft.panilla.forge112.io.PacketInspector(this);
                containerCleaner = new InventoryCleaner(this);
                break;
            default:
                logger.severe("Minecraft version " + minecraftVersion.getName() + " is not supported.");
                return;
        }

        // command registry | should this go here?
        CommandSpec panillaCommandSpec = CommandSpec.builder()
                .description(Text.of("Panilla information"))
                .executor(new PanillaCommand())
                .build();

        Sponge.getCommandManager().register(this, panillaCommandSpec);

        // listener registry | should this go here?
        Sponge.getGame().getEventManager().registerListeners(this, this);
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
        playerInjector.register(this, new SpongePanillaPlayer(player));
    }

    @Listener
    public void onClientConnectionDisconnect(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        playerInjector.unregister(new SpongePanillaPlayer(player));
    }

}
