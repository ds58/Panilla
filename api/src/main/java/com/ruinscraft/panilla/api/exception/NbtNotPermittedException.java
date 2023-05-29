package com.ruinscraft.panilla.api.exception;

public class NbtNotPermittedException extends Exception {

	private static final long serialVersionUID = 4005240262520128653L;

	public NbtNotPermittedException(String reason) {
		super(reason);
	}
	
}
