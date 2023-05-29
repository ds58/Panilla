package com.ruinscraft.panilla.v1_13_R2;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.ruinscraft.panilla.api.IContainerCleaner;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.checks.NbtChecks;
import com.ruinscraft.panilla.v1_13_R2.nbt.NbtTagCompound;

import net.minecraft.server.v1_13_R2.Container;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class ContainerCleaner implements IContainerCleaner {

	private final IProtocolConstants protocolConstants;
	private final PStrictness strictness;

	public ContainerCleaner(PStrictness strictness, IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
		this.strictness = strictness;
	}

	@Override
	public void clean(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		Container container = craftPlayer.getHandle().activeContainer;

		for (int slot = 0; slot < container.slots.size(); slot++) {
			ItemStack itemStack = container.getSlot(slot).getItem();
			
			if (itemStack == null || !itemStack.hasTag()) continue;
			
			NBTTagCompound tag = itemStack.getTag();

			String failedNbt = NbtChecks.checkAll(new NbtTagCompound(tag),
					itemStack.getItem().getClass().getSimpleName(), protocolConstants, strictness);

			if (failedNbt != null) {
				tag.remove(failedNbt);
				container.getSlot(slot).getItem().setTag(tag);
			}
		}
	}

}
