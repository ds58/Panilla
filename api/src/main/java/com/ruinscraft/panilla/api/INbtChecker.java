package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

public interface INbtChecker {

	void check_Item(Object object) throws NbtNotPermittedException;
	void check_ench(Object object) throws NbtNotPermittedException;
	void check_display(Object object) throws NbtNotPermittedException;
	void check_AttributeModifiers(Object object) throws NbtNotPermittedException;
	void check_Unbreakable(Object object) throws NbtNotPermittedException;
	void check_SkullOwner(Object object) throws NbtNotPermittedException;
	void check_HideFlags(Object object) throws NbtNotPermittedException;
	void check_CanDestroy(Object object) throws NbtNotPermittedException;
	void check_PickupDelay(Object object) throws NbtNotPermittedException;
	void check_Age(Object object) throws NbtNotPermittedException;
	void check_generation(Object object) throws NbtNotPermittedException;
	void check_CanPlaceOn(Object object) throws NbtNotPermittedException;
	void check_BlockEntityTag(Object object) throws NbtNotPermittedException;
	void check_CustomPotionEffects(Object object) throws NbtNotPermittedException;
	void check_Potion(Object object) throws NbtNotPermittedException;
	void check_CustomPotionColor(Object object) throws NbtNotPermittedException;

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