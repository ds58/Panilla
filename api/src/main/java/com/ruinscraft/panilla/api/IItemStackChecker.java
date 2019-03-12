package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.InvalidDamageException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.InvalidItemStackSize;

public interface IItemStackChecker {

	void checkCount(Object object) throws InvalidItemStackSize;
	void checkDamage(Object object) throws InvalidDamageException;
	void checkRecursiveNbt(Object object, INbtChecker nbtChecker) throws NbtNotPermittedException;
	
	default void checkAll(Object object, PStrictness strictness, INbtChecker nbtChecker)
			throws InvalidItemStackSize, InvalidDamageException, NbtNotPermittedException {

		switch (strictness) {
		case STRICT:	// petty
		case AVERAGE:	// somewhat abusive
			checkCount(object);
			checkDamage(object);
		case LENIENT:	// game breaking
			checkRecursiveNbt(object, nbtChecker);
		}
	}
	
}
