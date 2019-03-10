package com.ruinscraft.panilla.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class PanillaConfig {

	public boolean consoleLogging = true;
	public boolean chatLogging = false;
	public Strictness strictness = Strictness.AVERAGE;
	public List<String> nbtWhitelist = new ArrayList<>();
	public List<Integer> packetWhitelist =new ArrayList<>();

	public static PanillaConfig build(FileConfiguration fileConfiguration) {
		PanillaConfig panillaConfig = new PanillaConfig();

		panillaConfig.consoleLogging = fileConfiguration.getBoolean("logging.console");
		panillaConfig.chatLogging = fileConfiguration.getBoolean("logging.chat");
		panillaConfig.strictness = Strictness.valueOf(fileConfiguration.getString("strictness").toUpperCase());
		panillaConfig.nbtWhitelist = fileConfiguration.getStringList("nbt-whitelist");
		panillaConfig.packetWhitelist = fileConfiguration.getIntegerList("packet-whitelist");

		return panillaConfig;
	}

}
