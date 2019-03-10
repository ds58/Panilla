package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

public interface INbtChecker {

	boolean check_Item(Object object);
	boolean check_ench(Object object);
	boolean check_display(Object object);
	boolean check_AttributeModifiers(Object object);
	boolean check_Unbreakable(Object object);
	boolean check_SkullOwner(Object object);
	boolean check_HideFlags(Object object);
	boolean check_CanDestroy(Object object);
	boolean check_PickupDelay(Object object);
	boolean check_Age(Object object);
	boolean check_generation(Object object);
	boolean check_CanPlaceOn(Object object);
	boolean check_BlockEntityTag(Object object);
	boolean check_CustomPotionEffects(Object object);
	boolean check_Potion(Object object);
	boolean check_CustomPotionColor(Object object);

	default void check(Object object) throws NbtNotPermittedException {
		check_Item(object);
		check_ench(object);
		check_display(object);
		check_AttributeModifiers(object);
		check_Unbreakable(object);
		check_SkullOwner(object);
		check_HideFlags(object);
		check_CanDestroy(object);
		check_PickupDelay(object);
		check_Age(object);
		check_generation(object);
		check_CanPlaceOn(object);
		check_BlockEntityTag(object);
		check_CustomPotionEffects(object);
		check_Potion(object);
		check_CustomPotionColor(object);
	}

}
