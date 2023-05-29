package com.ruinscraft.panilla.api;

import java.util.List;

import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

// https://minecraft.gamepedia.com/Player.dat_format
public interface INbtChecker {

	/* general */
	// TODO: damage
	void check_Unbreakable(Object object) throws NbtNotPermittedException;
	void check_CanDestroy(Object object) throws NbtNotPermittedException;

	/* block tags */
	void check_CanPlaceOn(Object object) throws NbtNotPermittedException;
	void check_BlockEntityTag(Object object) throws NbtNotPermittedException;
	void check_BlockStateTag(Object object) throws NbtNotPermittedException;

	/* enchantments */
	void check_ench(Object object) throws NbtNotPermittedException;
	default void check_Enchantments(Object object) throws NbtNotPermittedException {
		check_ench(object);
	}
	void check_StoredEnchantments(Object object) throws NbtNotPermittedException;
	void check_RepairCost(Object object) throws NbtNotPermittedException;

	/* attribute modifiers */
	void check_AttributeModifiers(Object object) throws NbtNotPermittedException;

	/* potion effects */
	void check_CustomPotionEffects(Object object) throws NbtNotPermittedException;
	void check_Potion(Object object) throws NbtNotPermittedException;
	void check_CustomPotionColor(Object object) throws NbtNotPermittedException;

	/* display properties */
	void check_display(Object object) throws NbtNotPermittedException;
	void check_HideFlags(Object object) throws NbtNotPermittedException;

	/* written books */
	void check_resolved(Object object) throws NbtNotPermittedException;
	void check_generation(Object object) throws NbtNotPermittedException;
	void check_author(Object object) throws NbtNotPermittedException;
	void check_title(Object object) throws NbtNotPermittedException;
	void check_pages(Object object) throws NbtNotPermittedException;

	/* player heads */
	void check_SkullOwner(Object object) throws NbtNotPermittedException;

	/* fireworks */
	void check_Explosion(Object object) throws NbtNotPermittedException;
	void check_Fireworks(Object object) throws NbtNotPermittedException;

	/* armor stands/spawn eggs/buckets of fish */
	void check_EntityTag(Object object) throws NbtNotPermittedException;

	/* buckets of fish */
	void check_BucketVariantTag(Object object) throws NbtNotPermittedException;

	/* maps */
	void check_map(Object object) throws NbtNotPermittedException;
	void check_map_scale_direction(Object object) throws NbtNotPermittedException;
	void check_Decorations(Object object) throws NbtNotPermittedException;

	/* suspicious stew */
	void check_Effects(Object object) throws NbtNotPermittedException;

	default void checkForNotValid(List<String> tags) throws NbtNotPermittedException {
		try {
			for (String tag : tags) {
				getClass().getMethod("check_" + tag, Object.class); // TODO: possible performance hit
			}
		} catch (NoSuchMethodException | SecurityException e) {
			throw new NbtNotPermittedException("invalid nbt tag");
		}
	}

	// TODO: fill out
	default void checkAll(Object object, PStrictness strictness) throws NbtNotPermittedException {
		switch (strictness) {
		case STRICT:	// petty
		case AVERAGE:	// somewhat abusive
		case LENIENT:	// game breaking
		}
	}

}
