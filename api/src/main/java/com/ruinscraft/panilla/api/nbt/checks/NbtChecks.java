package com.ruinscraft.panilla.api.nbt.checks;

import java.util.HashMap;
import java.util.Map;

public final class NbtChecks {

	private static final Map<String, NbtCheck> checks = new HashMap<>();

	static {
		registerCheck(new NbtCheck_Unbreakable());
		registerCheck(new NbtCheck_CanDestroy());
		registerCheck(new NbtCheck_CanPlaceOn());
		registerCheck(new NbtCheck_BlockEntityTag());
		registerCheck(new NbtCheck_BlockStateTag());
		registerCheck(new NbtCheck_ench());
		registerCheck(new NbtCheck_Enchantments());
		registerCheck(new NbtCheck_StoredEnchantments());
		registerCheck(new NbtCheck_RepairCost());
		registerCheck(new NbtCheck_AttributeModifiers());
		registerCheck(new NbtCheck_CustomPotionEffects());
		registerCheck(new NbtCheck_Potion());
		registerCheck(new NbtCheck_CustomPotionColor());
	}
	
	public static void registerCheck(NbtCheck check) {
		checks.put(check.getName(), check);
	}
	
	public static NbtCheck getCheck(String tag) {
		return checks.get(tag);
	}
	
	public static boolean exists(String tag) {
		return checks.containsKey(tag);
	}
	
}
