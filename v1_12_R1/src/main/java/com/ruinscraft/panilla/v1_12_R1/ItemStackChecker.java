package com.ruinscraft.panilla.v1_12_R1;

import com.ruinscraft.panilla.api.IItemStackChecker;
import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.InvalidDamageException;
import com.ruinscraft.panilla.api.exception.InvalidItemStackSize;

import net.minecraft.server.v1_12_R1.ItemStack;

public class ItemStackChecker implements IItemStackChecker {

	private final IProtocolConstants protocolConstants;

	public ItemStackChecker(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}

	@Override
	public void checkCount(Object object) throws InvalidItemStackSize {
		if (object instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) object;

			if (itemStack.getCount() > itemStack.getMaxStackSize()) {
				throw new InvalidItemStackSize();
			}
		}
	}
	
	// TODO: check logic
	@Override
	public void checkDamage(Object object) throws InvalidDamageException {
		if (object instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) object;

			/* If damage < 0 || damage > max durability */
			if (itemStack.i() < 0 || itemStack.i() > itemStack.k()) {
				throw new InvalidDamageException();
			}
		}
	}

	@Override
	public void checkRecursiveNbt(Object object, INbtChecker nbtChecker) throws NbtNotPermittedException {
		if (object instanceof ItemStack) {
			ItemStack itemStack = (ItemStack) object;
			
		}
	}

}
