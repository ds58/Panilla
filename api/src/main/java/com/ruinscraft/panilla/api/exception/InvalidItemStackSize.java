package com.ruinscraft.panilla.api.exception;

public class InvalidItemStackSize extends Exception {

	private static final long serialVersionUID = 8361214376069884055L;

	public InvalidItemStackSize() {
		super("too many items in an ItemStack");
	}
	
}
