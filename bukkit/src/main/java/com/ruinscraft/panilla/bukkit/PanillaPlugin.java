package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.*;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PFiles;
import com.ruinscraft.panilla.api.config.PLocale;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PanillaPlugin extends JavaPlugin implements IPanilla, IPanillaPlatform {

    /* singleton ========================================== */
    private static PanillaPlugin singleton;
    private PanillaLogger panillaLogger;
    private PConfig pConfig;
    private PLocale pLocale;
    private IProtocolConstants protocolConstants;
    private IPlayerInjector playerInjector;
    private IPacketInspector packetInspector;
    private IContainerCleaner containerCleaner;
    private IEnchantments enchantments;

    public static PanillaPlugin get() {
        return singleton;
    }

    @Override
    public PanillaLogger getPanlliaLogger() {
        return panillaLogger;
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
        Collection<IPanillaPlayer> panillaPlayers = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach(p -> panillaPlayers.add(new BukkitPanillaPlayer(p)));
        return panillaPlayers;
    }

    @Override
    public void runTaskLater(long delay, Runnable task) {
        getServer().getScheduler().runTaskLater(this, task, delay);
    }

    private synchronized void loadConfig() throws IOException {
        PFiles.saveResource("config.yml", getDataFolder());

        pConfig = new PConfig();

        pConfig.localeFile = getConfig().getString("locale-file", pConfig.localeFile);
        pConfig.consoleLogging = getConfig().getBoolean("logging.console");
        pConfig.chatLogging = getConfig().getBoolean("logging.chat");
        pConfig.strictness = PStrictness.valueOf(getConfig().getString("strictness", pConfig.strictness.name()).toUpperCase());
        pConfig.preventMinecraftEducationSkulls = getConfig().getBoolean("prevent-minecraft-education-skulls", false);
        pConfig.preventFaweBrushNbt = getConfig().getBoolean("prevent-fawe-brush-nbt", false);
        pConfig.nbtWhitelist = getConfig().getStringList("nbt-whitelist");
        pConfig.disabledWorlds = getConfig().getStringList("disabled-worlds");
        pConfig.maxNonMinecraftNbtKeys = getConfig().getInt("max-non-minecraft-nbt-keys", pConfig.maxNonMinecraftNbtKeys);
    }

    private synchronized void loadLocale(String localeFileName) throws IOException {
        PFiles.saveResource(localeFileName, getDataFolder());

        YamlConfiguration yaml =
                YamlConfiguration.loadConfiguration(new File(getDataFolder(), localeFileName));
        Map<String, String> translations = new HashMap<>();

        for (String key : yaml.getKeys(false)) {
            translations.put(key, yaml.getString(key));
        }

        // load default english translations
        YamlConfiguration defaultEnglishYaml =
                YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("en_US.yml")));
        Map<String, String> defaultTranslations = new HashMap<>();

        for (String key : defaultEnglishYaml.getKeys(false)) {
            defaultTranslations.put(key, defaultEnglishYaml.getString(key));
        }

        pLocale = new PLocale(translations, defaultTranslations);
    }

    // TODO: what happens with glowstone?
    private String v_Version() {
        return getServer().getClass().getPackage().getName().substring("org.bukkit.craftbukkit.".length());
    }

    @Override
    public void onEnable() {
        singleton = this;

        getDataFolder().mkdirs();

        try {
            loadConfig();
        } catch (IOException e) {
            getLogger().warning("Could not load config file");
            e.printStackTrace();
        }

        try {
            loadLocale(pConfig.localeFile);
        } catch (IOException e) {
            getLogger().warning("Could not load Locale file: " + pConfig.localeFile);
            e.printStackTrace();
        }

        panillaLogger = new PanillaLogger(this);
        enchantments = new BukkitEnchantments();

        final String v_Version = v_Version();

        // TODO: add glowstone support

        switch (v_Version) {
            case "v1_8_R3":
                protocolConstants = new DefaultProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.io.PlayerInjector();
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_8_R3.ContainerCleaner(this);
                break;
            case "v1_12_R1":
                protocolConstants = new DefaultProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.io.PlayerInjector();
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_12_R1.ContainerCleaner(this);
                break;
            case "v1_13_R2":
                protocolConstants = new DefaultProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.io.PlayerInjector();
                packetInspector = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.io.PacketInspector(this);
                containerCleaner = new com.ruinscraft.panilla.craftbukkit.v1_13_R2.ContainerCleaner(this);
                break;
            case "v1_14_R1":
                protocolConstants = new DefaultProtocolConstants();
                playerInjector = new com.ruinscraft.panilla.craftbukkit.v1_14_R1.io.PlayerInjector();
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
            playerInjector.register(this, new BukkitPanillaPlayer(player));
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

}
