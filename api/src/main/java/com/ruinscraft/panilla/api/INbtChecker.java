package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

// https://minecraft.gamepedia.com/Player.dat_format
public interface INbtChecker {

	/* general */
	// TODO: damage
	void check_Unbreakable(Object _tag) throws NbtNotPermittedException;
	void check_CanDestroy(Object _tag) throws NbtNotPermittedException;

	/* block _tags */
	void check_CanPlaceOn(Object _tag) throws NbtNotPermittedException;
	void check_BlockEntityTag(Object _tag) throws NbtNotPermittedException;
	void check_BlockStateTag(Object _tag) throws NbtNotPermittedException;

	/* enchantments */
	void check_ench(Object _tag) throws NbtNotPermittedException;
	default void check_Enchantments(Object _tag) throws NbtNotPermittedException {
		check_ench(_tag);
	}
	void check_StoredEnchantments(Object _tag) throws NbtNotPermittedException;
	void check_RepairCost(Object _tag) throws NbtNotPermittedException;

	/* attribute modifiers */
	void check_AttributeModifiers(Object _tag) throws NbtNotPermittedException;

	/* potion effects */
	void check_CustomPotionEffects(Object _tag) throws NbtNotPermittedException;
	void check_Potion(Object _tag) throws NbtNotPermittedException;
	void check_CustomPotionColor(Object _tag) throws NbtNotPermittedException;

	/* display properties */
	void check_display(Object _tag) throws NbtNotPermittedException;
	void check_HideFlags(Object _tag) throws NbtNotPermittedException;

	/* written books */
	void check_resolved(Object _tag) throws NbtNotPermittedException;
	void check_generation(Object _tag) throws NbtNotPermittedException;
	void check_author(Object _tag) throws NbtNotPermittedException;
	void check_title(Object _tag) throws NbtNotPermittedException;
	void check_pages(Object _tag) throws NbtNotPermittedException;

	/* player heads */
	void check_SkullOwner(Object _tag) throws NbtNotPermittedException;

	/* fireworks */
	void check_Explosion(Object _tag) throws NbtNotPermittedException;
	void check_Fireworks(Object _tag) throws NbtNotPermittedException;

	/* armor stands/spawn eggs/buckets of fish */
	void check_EntityTag(Object _tag) throws NbtNotPermittedException;

	/* buckets of fish */
	void check_BucketVariantTag(Object _tag) throws NbtNotPermittedException;

	/* maps */
	void check_map(Object _tag) throws NbtNotPermittedException;
	void check_map_scale_direction(Object _tag) throws NbtNotPermittedException;
	void check_Decorations(Object _tag) throws NbtNotPermittedException;

	/* suspicious stew */
	void check_Effects(Object _tag) throws NbtNotPermittedException;

	void checkForNotValid(Object _tag) throws NbtNotPermittedException;
	
	default void checkAll(Object _tag, PStrictness strictness) throws NbtNotPermittedException {
		switch (strictness) {
		case STRICT:	// petty
			check_display(_tag);
			check_HideFlags(_tag);
		case AVERAGE:	// somewhat abusive
			check_Unbreakable(_tag);
			check_CanDestroy(_tag);
			check_CanPlaceOn(_tag);
			check_BlockStateTag(_tag);
			check_ench(_tag);
			check_Enchantments(_tag);
			check_StoredEnchantments(_tag);
			check_RepairCost(_tag);
			check_AttributeModifiers(_tag);
			check_Potion(_tag);
			check_CustomPotionColor(_tag);
			check_resolved(_tag);
			check_generation(_tag);
			check_author(_tag);
			check_title(_tag);
			check_SkullOwner(_tag);
			check_Explosion(_tag);
			check_Fireworks(_tag);
			check_EntityTag(_tag);
			check_BucketVariantTag(_tag);
			check_map(_tag);
			check_map_scale_direction(_tag);
			check_Decorations(_tag);
			check_Effects(_tag);
		case LENIENT:	// game breaking
			check_BlockEntityTag(_tag);
			check_CustomPotionEffects(_tag);
			check_pages(_tag);
			checkForNotValid(_tag);
		}
	}

}
