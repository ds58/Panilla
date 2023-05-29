package com.ruinscraft.panilla.api.exception;

public class UnderstackedItemStackException extends Exception {

	private static final long serialVersionUID = 8213927677445165936L;

	public UnderstackedItemStackException() {
		super("too few items in an ItemStack");
	}
	
}
