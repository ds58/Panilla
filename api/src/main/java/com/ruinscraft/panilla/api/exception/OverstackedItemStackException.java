package com.ruinscraft.panilla.api.exception;

public class OverstackedItemStackException extends Exception {

	private static final long serialVersionUID = 8361214376069884055L;

	public OverstackedItemStackException() {
		super("too many items in an ItemStack");
	}
	
}
