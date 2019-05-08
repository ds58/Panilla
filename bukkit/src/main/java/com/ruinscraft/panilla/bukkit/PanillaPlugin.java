package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.*;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PLocale;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PanillaPlugin extends JavaPlugin implements IPanilla, IPanillaPlatform {

    private PConfig pConfig;
    private PLocale pLocale;
    private IProtocolConstants protocolConstants;
    private IPlayerInjector playerInjector;
    private IPacketInspector packetInspector;
    private IContainerCleaner containerCleaner;
    private IEnchantments enchantments;

    @Override
    public String getVersionString() {
        return getDescription().getVersion();
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
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public boolean isValidPotionColor(int bgr) {
        try {
            Color.fromBGR(bgr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Collection<IPanillaPlayer> getOnlinePlayers() {
        Collection<IPanillaPlayer> panillaPlayers = Collections.emptySet();
        Bukkit.getOnlinePlayers().forEach(p -> panillaPlayers.add(new BukkitPanillaPlayer(p)));
        return panillaPlayers;
    }

    private synchronized void loadConfig() {
        saveDefaultConfig();

        pConfig = new PConfig();

        pConfig.localeFile = getConfig().getString("locale-file", pConfig.localeFile);
        pConfig.consoleLogging = getConfig().getBoolean("logging.console");
        pConfig.chatLogging = getConfig().getBoolean("logging.chat");
        pConfig.strictness = PStrictness.valueOf(getConfig().getString("strictness", pConfig.strictness.name()).toUpperCase());
        pConfig.nbtWhitelist = getConfig().getStringList("nbt-whitelist");
        pConfig.maxNonMinecraftNbtKeys = getConfig().getInt("max-non-minecraft-nbt-keys", pConfig.maxNonMinecraftNbtKeys);
    }

    private synchronized void loadLocale(String localeFileName) throws IOException {
        getDataFolder().mkdirs();

        File file = new File(getDataFolder(), localeFileName);

        if (!file.exists()) {
            PLocale.saveDefaultLocaleFile(localeFileName, getDataFolder());
        }

        YamlConfiguration yaml = new YamlConfiguration();

        try {
            yaml.load(file);
        } catch (InvalidConfigurationException e) {
            throw new IOException(e.getMessage());
        }

        Map<String, String> translations = new HashMap<>();

        for (String key : yaml.getKeys(false)) {
            translations.put(key, yaml.getString(key));
        }

        pLocale = new PLocale(translations);
    }

    private String v_Version() {
        return getServer().getClass().getPackage().getName().substring("org.bukkit.craftbukkit.".length());
    }

    @Override
    public void onEnable() {
        singleton = this;

        loadConfig();
        try {
            loadLocale(pConfig.localeFile);
        } catch (IOException e) {
            getLogger().warning("Could not load Locale file: " + pConfig.localeFile);
            e.printStackTrace();
        }

        PanillaLogger panillaLogger = new PanillaLogger(this);

        enchantments = new BukkitEnchantments();

        final String v_Version = v_Version();

        switch (v_Version) {
            case "v1_8_R3":
                protocolConstants = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.ProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.io.PlayerInjector(this, panillaLogger);
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.ContainerCleaner(this);
                break;
            case "v1_12_R1":
                protocolConstants = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.ProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.io.PlayerInjector(this, panillaLogger);
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.ContainerCleaner(this);
                break;
            case "v1_13_R2":
                protocolConstants = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.ProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.io.PlayerInjector(this, panillaLogger);
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.ContainerCleaner(this);
                break;
            case "v1_14_R1":
                protocolConstants = new com.ruinscraft.panilla.craftbukkit.v1_14_R1.ProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_14_R1.io.PlayerInjector(this, panillaLogger);
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_14_R1.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_14_R1.ContainerCleaner(this);
                break;
            default:
                getLogger().severe("Minecraft version " + v_Version + " is not supported.");
                getServer().getPluginManager().disablePlugin(this);
                return;
        }

        /* Register listeners */
        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);

        /* Register commands */
        getCommand("panilla").setExecutor(new PanillaCommand());

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerInjector.register(new BukkitPanillaPlayer(player));
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerInjector.unregister(new BukkitPanillaPlayer(player));
        }

        singleton = null;
    }

    /* singleton ========================================== */
    private static PanillaPlugin singleton;

    public static PanillaPlugin get() {
        return singleton;
    }
    /* singleton ========================================== */

}
