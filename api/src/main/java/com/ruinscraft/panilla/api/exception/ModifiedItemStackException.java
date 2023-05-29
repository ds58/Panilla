package com.ruinscraft.panilla.api.exception;

import java.util.List;

public class ModifiedItemStackException extends Exception {

	private static final long serialVersionUID = 3407117091999084766L;

	private final List<String> modifications;
	
	public ModifiedItemStackException(List<String> modifications) {
		this.modifications = modifications;
	}
	
	public List<String> getModifications() {
		return modifications;
	}
	
	@Override
	public String getMessage() {
		return "ItemStack contained modifications: " + String.join(", ", modifications);
	}
	
}
