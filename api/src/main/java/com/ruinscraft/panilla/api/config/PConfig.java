package com.ruinscraft.panilla.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PConfig {

    public static String PERMISSION_LOG_CHAT = "panilla.log.chat";
    public static String PERMISSION_BYPASS = "panilla.bypass";

    /* Defaults */
    public String language = "en";
    public boolean consoleLogging = true;
    public boolean chatLogging = false;
    public PStrictness strictness = PStrictness.AVERAGE;
    public boolean preventMinecraftEducationSkulls = false;
    public boolean preventFaweBrushNbt = false;
    public boolean ignoreNonPlayerInventories = false;
    public boolean noBlockEntityTag = false;
    public List<String> nbtWhitelist = new ArrayList<>();
    public List<String> disabledWorlds = new ArrayList<>();
    public int maxNonMinecraftNbtKeys = 16;
    public boolean overrideMinecraftMaxEnchantmentLevels = false;
    public Map<String, Integer> minecraftMaxEnchantmentLevelOverrides = new HashMap<>();

}
