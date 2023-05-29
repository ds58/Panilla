package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OverstackedItemStackException;
import com.ruinscraft.panilla.api.exception.UnderstackedItemStackException;

public interface IItemStackChecker {

	void checkTooMany(Object object) throws OverstackedItemStackException;
	void checkTooFew(Object object) throws UnderstackedItemStackException;
	void checkRecursiveNbt(Object object, INbtChecker nbtChecker) throws NbtNotPermittedException;
	
	default void checkAll(Object object, PStrictness strictness, INbtChecker nbtChecker)
			throws OverstackedItemStackException, UnderstackedItemStackException, NbtNotPermittedException {

		switch (strictness) {
		case STRICT:	// petty
		case AVERAGE:	// somewhat abusive
			checkTooFew(object);
			checkTooMany(object);
		case LENIENT:	// game breaking
			checkRecursiveNbt(object, nbtChecker);
		}
	}
	
}
