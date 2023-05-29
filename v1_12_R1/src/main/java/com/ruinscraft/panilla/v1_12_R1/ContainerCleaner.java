package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.v1_12_R1.nbt.NbtTagCompound;

import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ContainerCleaner implements IContainerCleaner {

	private final IProtocolConstants protocolConstants;
	private final PStrictness strictness;

	public ContainerCleaner(PStrictness strictness, IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
		this.strictness = strictness;
	}

	@Override
	public void clean(Object _craftPlayer) {
		if (_craftPlayer instanceof CraftPlayer) {
			CraftPlayer craftPlayer = (CraftPlayer) _craftPlayer;

			Container container = craftPlayer.getHandle().activeContainer;

			for (int slot = 0; slot < container.slots.size(); slot++) {
				ItemStack itemStack = container.getSlot(slot).getItem();
				NBTTagCompound tag = itemStack.getTag();

				String failedNbt = NbtChecks.checkAll(new NbtTagCompound(tag),
						itemStack.getItem().getClass().getSimpleName(),
						protocolConstants,
						strictness);

				if (failedNbt != null) {
					tag.remove(failedNbt);
					container.getSlot(slot).getItem().setTag(tag);
				}
			}
		}
	}

}
