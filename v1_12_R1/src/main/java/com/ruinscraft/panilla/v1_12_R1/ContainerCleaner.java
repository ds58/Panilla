package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.config.PStrictness;

import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ContainerCleaner implements IContainerCleaner {

	private final PStrictness strictness;
	private final INbtChecker nbtChecker;

	public ContainerCleaner(PStrictness strictness, INbtChecker nbtChecker) {
		this.strictness = strictness;
		this.nbtChecker = nbtChecker;
	}

	@Override
	public void clean(Object _craftPlayer) {
		if (_craftPlayer instanceof CraftPlayer) {
			CraftPlayer craftPlayer = (CraftPlayer) _craftPlayer;

			Container container = craftPlayer.getHandle().activeContainer;

			for (int slot = 0; slot < container.slots.size(); slot++) {
				NBTTagCompound tag = container.getSlot(slot).getItem().getTag();

				String failedNbt = nbtChecker.checkAll(tag, strictness);

				if (failedNbt != null) {
					tag.remove(failedNbt);
					container.getSlot(slot).getItem().setTag(tag);
				}
			}
		}
	}

}
