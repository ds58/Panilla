package com.ruinscraft.panilla.v1_12_R1;

import com.ruinscraft.panilla.api.IItemStackChecker;
import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OverstackedItemStackException;
import com.ruinscraft.panilla.api.exception.UnderstackedItemStackException;

import net.minecraft.server.v1_12_R1.ItemStack;

public class ItemStackChecker implements IItemStackChecker {

	@Override
	public void checkTooMany(Object object) throws OverstackedItemStackException {
		if (object instanceof ItemStack) {
			
		}
	}

	@Override
	public void checkTooFew(Object object) throws UnderstackedItemStackException {
		if (object instanceof ItemStack) {
			
		}
	}

	@Override
	public void checkRecursiveNbt(Object object, INbtChecker nbtChecker) throws NbtNotPermittedException {
		if (object instanceof ItemStack) {
			
		}
	}

}
