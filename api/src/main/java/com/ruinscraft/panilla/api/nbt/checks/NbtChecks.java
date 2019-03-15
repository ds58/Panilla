package com.ruinscraft.panilla.api.nbt.checks;

import java.util.HashMap;
import java.util.Map;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public final class NbtChecks {

	private static final Map<String, NbtCheck> checks = new HashMap<>();

	static {
		register(new NbtCheck_Unbreakable());
		register(new NbtCheck_CanDestroy());
		register(new NbtCheck_CanPlaceOn());
		register(new NbtCheck_BlockEntityTag());
		register(new NbtCheck_BlockStateTag());
		register(new NbtCheck_ench());
		register(new NbtCheck_Enchantments());
		register(new NbtCheck_StoredEnchantments());
		register(new NbtCheck_RepairCost());
		register(new NbtCheck_AttributeModifiers());
		register(new NbtCheck_CustomPotionEffects());
		register(new NbtCheck_Potion());
		register(new NbtCheck_CustomPotionColor());
		register(new NbtCheck_display());
	}
	
	public static void register(NbtCheck check) {
		checks.put(check.getName(), check);
	}
	
	public static NbtCheck get(String tag) {
		return checks.get(tag);
	}
	
	public static boolean exists(String tag) {
		return checks.containsKey(tag);
	}
	
	public static void checkPacketPlayIn(INbtTagCompound tag,
			String nmsItemClassName,
			String nmsPacketClassName,
			IProtocolConstants protocolConstants,
			PStrictness strictness) throws NbtNotPermittedException {

		String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, strictness);
		
		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsPacketClassName, true, failedNbt);
		}
	}
	
	public static void checkPacketPlayOut(INbtTagCompound tag,
			String nmsItemClassName,
			String nmsPacketClassName,
			IProtocolConstants protocolConstants,
			PStrictness strictness) throws NbtNotPermittedException {

		String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, strictness);
		
		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsPacketClassName, false, failedNbt);
		}
	}
	
	public static String checkAll(INbtTagCompound tag,
			String nmsItemClassName,
			IProtocolConstants protocolConstants,
			PStrictness strictness) {
		for (String subTag : checks.keySet()) {
			if (tag.hasKey(subTag)) {
				NbtCheck check = get(subTag);
				
				if (check.getTolerance().lvl > strictness.lvl) {
					continue;
				}
				
				boolean pass = get(subTag).check(tag, nmsItemClassName, protocolConstants);
				
				if (!pass) return subTag;
			}
		}
		
		return null;
	}
	
}
