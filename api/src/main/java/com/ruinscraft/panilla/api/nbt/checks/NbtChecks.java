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
		register(new NbtCheck_HideFlags());
		register(new NbtCheck_resolved());
		register(new NbtCheck_generation());
		register(new NbtCheck_author());
		register(new NbtCheck_title());
		register(new NbtCheck_pages());
		register(new NbtCheck_SkullOwner());
		register(new NbtCheck_Explosion());
		register(new NbtCheck_Fireworks());
		register(new NbtCheck_EntityTag());
		register(new NbtCheck_BucketVariantTag());
		register(new NbtCheck_map());
		register(new NbtCheck_map_scale_direction());
		register(new NbtCheck_Decorations());
		register(new NbtCheck_Effects());
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

	public static void checkPacketPlayIn(INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
			IProtocolConstants protocolConstants, PStrictness strictness) throws NbtNotPermittedException {

		String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, strictness);

		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsPacketClassName, true, failedNbt);
		}
	}

	public static void checkPacketPlayOut(INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
			IProtocolConstants protocolConstants, PStrictness strictness) throws NbtNotPermittedException {

		String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, strictness);
		
		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsPacketClassName, false, failedNbt);
		}
	}

	public static String checkAll(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants,
			PStrictness strictness) {
		for (String key : tag.getKeys()) {
			NbtCheck check = checks.get(key);

			if (check == null) {
				return key;
			}

			if (check.getTolerance().lvl > strictness.lvl) {
				continue;
			}

			if (!check.check(tag, nmsItemClassName, protocolConstants)) {
				return key;
			}
		}

		return null;
	}

}
