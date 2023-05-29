package com.ruinscraft.panilla.v1_12_R1;

import com.ruinscraft.panilla.api.IItemStackChecker;
import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.OverstackedItemStackException;
import com.ruinscraft.panilla.api.exception.UnderstackedItemStackException;

import net.minecraft.server.v1_12_R1.ItemStack;

public class ItemStackChecker implements IItemStackChecker {

	private final IProtocolConstants protocolConstants;
	
	public ItemStackChecker(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}
	
	@Override
	public void checkTooMany(Object object) throws OverstackedItemStackException {
		if (object instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) object;

			if (itemStack.getCount() > itemStack.getMaxStackSize()) {
				throw new OverstackedItemStackException();
			}
		}
	}

	@Override
	public void checkTooFew(Object object) throws UnderstackedItemStackException {
		if (object instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) object;
			
			if (itemStack.getCount() < protocolConstants.minStackSize()) {
				throw new UnderstackedItemStackException();
			}
		}
	}

	@Override
	public void checkRecursiveNbt(Object object, INbtChecker nbtChecker) throws NbtNotPermittedException {
		if (object instanceof ItemStack) {
			
		}
	}

}
