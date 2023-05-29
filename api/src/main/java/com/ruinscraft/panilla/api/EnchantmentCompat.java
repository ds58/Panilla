package com.ruinscraft.panilla.api;

public enum EnchantmentCompat {

    AQUA_AFFINITY("minecraft:aqua_affinity", "WATER_WORKER", 6),
    BANE_OF_ARTHROPODS("minecraft:bane_of_arthropods", "DAMAGE_ARTHROPODS", 18),
    BINDING_CURSE("minecraft:binding_curse", "BINDING_CURSE", 10),
    BLAST_PROTECTION("minecraft:blast_protection", "PROTECTION_EXPLOSIONS", 3),
    CHANNELING("minecraft:channeling", "", 68), // 1.13
    DEPTH_STRIDER("minecraft:depth_strider", "DEPTH_STRIDER", 8),
    EFFICIENCY("minecraft:efficiency", "DIG_SPEED", 32),
    FEATHER_FALLING("minecraft:falling", "PROTECTION_FALL", 2),
    FIRE_ASPECT("minecraft:fire_aspect", "FIRE_ASPECT", 20),
    FIRE_PROTECTION("minecraft:fire_protection", "PROTECTION_FIRE", 1),
    FLAME("minecraft:flame", "ARROW_FIRE", 50),
    FORTUNE("minecraft:fortune", "LOOT_BONUS_BLOCKS", 35),
    FROST_WALKER("minecraft:frost_walker", "FROST_WALKER", 9),
    IMPALING("minecraft:impaling", "", 66), // 1.13
    INFINITY("minecraft:infinity", "ARROW_INFINITE", 51),
    KNOCKBACK("minecraft:knockback", "KNOCKBACK", 19),
    LOOTING("minecraft:looting", "LOOT_BONUS_MOBS", 21),
    LOYALTY("minecraft:loyalty", "", 65), // 1.13
    LUCK_OF_THE_SEA("minecraft:luck_of_the_sea", "LUCK", 61),
    LURE("minecraft:lure", "LURE", 62),
    MENDING("minecraft:mending", "MENDING", 70),
    POWER("minecraft:power", "ARROW_DAMAGE", 48),
    PROJECTILE_PROTECTION("minecraft:projectile_protection", "PROTECTION_PROJECTILE", 4),
    PROTECTION("minecraft:protection", "PROTECTION_ENVIRONMENTAL", 0),
    PUNCH("minecraft:punch", "ARROW_KNOCKBACK", 49),
    RESPIRATION("minecraft_respiration", "OXYGEN", 5),
    RIPTIDE("minecraft:riptide", "", 67), // 1.13
    SHARPNESS("minecraft:sharpness", "DAMAGE_ALL", 16),
    SILK_TOUCH("minecraft:silk_touch", "SILK_TOUCH", 33),
    SMITE("minecraft:smite", "DAMAGE_UNDEAD", 17),
    SWEEPING("minecraft:sweeping", "SWEEPING_EDGE", 22),
    THORNS("minecraft:thorns", "THORNS", 7),
    UNBREAKING("minecraft:unbreaking", "DURABILITY", 34),
    VANISHING_CURSE("minecraft:vanishing_curse", "VANISHING_CURSE", 71),

    // 1.14
    MULTISHOT("minecraft:multishot", "", -1),
    PIERCING("minecraft:piercing", "", -1),
    QUICK_CHARGE("minecraft:quick_charge", "", -1);

    public final String namedKey;
    public final String legacyName;
    public final int legacyId;

    EnchantmentCompat(String namedKey, String legacyName, int legacyId) {
        this.namedKey = namedKey;
        this.legacyName = legacyName;
        this.legacyId = legacyId;
    }

    public static EnchantmentCompat getByNamedKey(String namedKey) {
        namedKey = namedKey.toLowerCase();

        for (EnchantmentCompat enchantmentCompat : EnchantmentCompat.values()) {
            if (enchantmentCompat.namedKey.contains(namedKey)) {
                return enchantmentCompat;
            }
        }

        return null;
    }

    @Deprecated
    public static EnchantmentCompat getByLegacyName(String legacyName) {
        if (legacyName.isEmpty())
            return null;

        for (EnchantmentCompat enchantmentCompat : EnchantmentCompat.values()) {
            if (enchantmentCompat.legacyName.equals(legacyName)) {
                return enchantmentCompat;
            }
        }

        return null;
    }

    @Deprecated
    public static EnchantmentCompat getByLegacyId(int legacyId) {
        for (EnchantmentCompat enchantmentCompat : EnchantmentCompat.values()) {
            if (enchantmentCompat.legacyId == legacyId) {
                return enchantmentCompat;
            }
        }

        return null;
    }

}
