package com.ruinscraft.panilla.api;

public interface INbtChecker {

	void check_Item(Object object);
	void check_ench(Object object);
	void check_display(Object object);
	void check_AttributeModifiers(Object object);
	void check_Unbreakable(Object object);
	void check_SkullOwner(Object object);
	void check_HideFlags(Object object);
	void check_CanDestroy(Object object);
	void check_PickupDelay(Object object);
	void check_Age(Object object);
	void check_generation(Object object);
	void check_CanPlaceOn(Object object);
	void check_BlockEntityTag(Object object);
	void check_CustomPotionEffects(Object object);
	void check_Potion(Object object);
	void check_CustomPotionColor(Object object);

	default void check(Object object) {
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
