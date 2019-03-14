package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

// https://minecraft.gamepedia.com/Player.dat_format
public interface INbtChecker {

	/* general */
	// TODO: damage
	boolean check_Unbreakable(Object _tag);
	boolean check_CanDestroy(Object _tag);

	/* block _tags */
	boolean check_CanPlaceOn(Object _tag);
	boolean check_BlockEntityTag(Object _tag);
	boolean check_BlockStateTag(Object _tag);

	/* enchantments */
	boolean check_ench(Object _tag);
	default boolean check_Enchantments(Object _tag) {
		return check_ench(_tag);
	}
	default boolean check_StoredEnchantments(Object _tag) {
		return check_ench(_tag);
	}
	boolean check_RepairCost(Object _tag);

	/* attribute modifiers */
	boolean check_AttributeModifiers(Object _tag);

	/* potion effects */
	boolean check_CustomPotionEffects(Object _tag);
	boolean check_Potion(Object _tag);
	boolean check_CustomPotionColor(Object _tag);

	/* display properties */
	boolean check_display(Object _tag);
	boolean check_HideFlags(Object _tag);

	/* written books */
	boolean check_resolved(Object _tag);
	boolean check_generation(Object _tag);
	boolean check_author(Object _tag);
	boolean check_title(Object _tag);
	boolean check_pages(Object _tag);

	/* player heads */
	boolean check_SkullOwner(Object _tag);

	/* fireworks */
	boolean check_Explosion(Object _tag);
	boolean check_Fireworks(Object _tag);

	/* armor stands/spawn eggs/buckets of fish */
	boolean check_EntityTag(Object _tag);

	/* buckets of fish */
	boolean check_BucketVariantTag(Object _tag);

	/* maps */
	boolean check_map(Object _tag);
	boolean check_map_scale_direction(Object _tag);
	boolean check_Decorations(Object _tag);

	/* suspicious stew */
	boolean check_Effects(Object _tag);

	String checkForNotValid(Object _tag);

	default String checkAll(Object _tag, PStrictness strictness) {
		switch (strictness) {
		case STRICT:	// petty
			if (!check_display(_tag)) return "display";
			if (!check_HideFlags(_tag)) return "HideFlags";
		case AVERAGE:	// somewhat abusive
			if (!check_Unbreakable(_tag)) return "Unbreakable";
			if (!check_CanDestroy(_tag)) return "CanDestroy";
			if (!check_CanPlaceOn(_tag)) return "CanPlaceOn";
			if (!check_BlockStateTag(_tag)) return "BlockStateTag";
			if (!check_ench(_tag)) return "ench";
			if (!check_Enchantments(_tag)) return "Enchantments";
			if (!check_StoredEnchantments(_tag)) return "StoredEnchantments";
			if (!check_RepairCost(_tag)) return "RepairCost";
			if (!check_AttributeModifiers(_tag)) return "AttributeModifiers";
			if (!check_Potion(_tag)) return "Potion";
			if (!check_CustomPotionEffects(_tag)) return "CustomPotionEffects";
			if (!check_resolved(_tag)) return "resolved";
			if (!check_generation(_tag)) return "generation";
			if (!check_author(_tag)) return "author";
			if (!check_title(_tag)) return "title";
			if (!check_SkullOwner(_tag)) return "SkullOwner";
			if (!check_Explosion(_tag)) return "Explosion";
			if (!check_Fireworks(_tag)) return "Fireworks";
			if (!check_EntityTag(_tag)) return "EntityTag";
			if (!check_BucketVariantTag(_tag)) return "BucketVariantTag";
			if (!check_map(_tag)) return "map";
			if (!check_map_scale_direction(_tag)) return "map_scale_direction";
			if (!check_Decorations(_tag)) return "Decorations";
			if (!check_Effects(_tag)) return "Effects";
		case LENIENT:	// game breaking
			if (!check_BlockEntityTag(_tag)) return "BlockEntityTag";
			if (!check_CustomPotionColor(_tag)) return "CustomPotionColor";
			if (!check_pages(_tag)) return "pages";
			String invalid = checkForNotValid(_tag);
			if (invalid != null) return invalid;
		}
		
		return null;
	}

	default void checkPlayIn(Object _tag, PStrictness strictness, String nmsClass) throws NbtNotPermittedException {
		String failedNbt = checkAll(_tag, strictness);
		
		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsClass, true, failedNbt);
		}
	}

	default void checkPlayOut(Object _tag, PStrictness strictness, String nmsClass) throws NbtNotPermittedException {
		String failedNbt = checkAll(_tag, strictness);
		
		if (failedNbt != null) {
			throw new NbtNotPermittedException(nmsClass, false, failedNbt);
		}
	}

}
