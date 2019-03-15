package com.ruinscraft.panilla.api;

public enum EnchantmentCompat {

	AQUA_AFFINITY			("minecraft:aqua_affinity", 		6),
	BANE_OF_ARTHROPODS		("minecraft:bane_of_arthropods", 	18),
	BINDING_CURSE			("minecraft:binding_curse", 		10),
	BLAST_PROTECTION		("minecraft:blast_protection", 		3),
	CHANNELING				("minecraft:channeling", 			68),
	DEPTH_STRIDER			("minecraft:depth_strider", 		8),
	EFFICIENCY				("minecraft:efficiency", 			32),
	FEATHER_FALLING			("minecraft:falling", 				2),
	FIRE_ASPECT				("minecraft:fire_aspect", 			20),
	FIRE_PROTECTION			("minecraft:fire_protection", 		1),
	FLAME					("minecraft:flame", 				50),
	FORTUNE					("minecraft:fortune", 				35),
	FROST_WALKER			("minecraft:frost_walker", 			9),
	IMPALING				("minecraft:impaling", 				66),
	INFINITY				("minecraft:infinity", 				51),
	KNOCKBACK				("minecraft:knockback", 			19),
	LOOTING					("minecraft:looting", 				21),
	LOYALTY					("minecraft:loyalty",				65),
	LUCK_OF_THE_SEA			("minecraft:luck_of_the_sea",		61),
	LURE					("minecraft:lure", 					62),
	MENDING					("minecraft:mending", 				70),
	POWER					("minecraft:power", 				48),
	PROJECTILE_PROTECTION	("minecraft:projectile_protection", 4),
	PROTECTION				("minecraft:protection", 			0),
	PUNCH					("minecraft:punch", 				49),
	RESPIRATION				("minecraft_respiration", 			5),
	RIPTIDE					("minecraft:riptide", 				67),
	SHARPNESS				("minecraft:sharpness", 			16),
	SILK_TOUCH				("minecraft:silk_touch", 			33),
	SMITE					("minecraft:smite", 				17),
	SWEEPING				("minecraft:sweeping",				22),
	THORNS					("minecraft:thorns", 				7),
	UNBREAKING				("minecraft:unbreaking", 			34),
	VANISHING_CURSE			("minecraft:vanishing_curse", 		71);

	public final String minecraftName;
	public final int legacyId;

	private EnchantmentCompat(String minecraftName, int legacyId) {
		this.minecraftName = minecraftName;
		this.legacyId = legacyId;
	}

	public static EnchantmentCompat getById(int legacyId) {
		for (EnchantmentCompat enchantmentCompat : EnchantmentCompat.values()) {
			if (enchantmentCompat.legacyId == legacyId) {
				return enchantmentCompat;
			}
		}
		
		return null;
	}

	public static EnchantmentCompat getByName(String minecraftName) {
		for (EnchantmentCompat enchantmentCompat : EnchantmentCompat.values()) {
			if (enchantmentCompat.minecraftName.equals(minecraftName)) {
				return enchantmentCompat;
			}
		}
		
		return null;
	}

}
