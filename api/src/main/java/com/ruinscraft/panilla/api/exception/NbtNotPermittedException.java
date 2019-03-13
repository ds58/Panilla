package com.ruinscraft.panilla.api.exception;

public class NbtNotPermittedException extends Exception {

	private static final long serialVersionUID = 4005240262520128653L;

	private final String tagName;
	
	public NbtNotPermittedException(String tagName) {
		super("NBT tag " + tagName + " not permitted");
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
}
