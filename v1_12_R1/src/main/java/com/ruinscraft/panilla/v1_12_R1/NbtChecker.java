package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;

import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.NbtDataType;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class NbtChecker implements INbtChecker {

	private final IProtocolConstants protocolConstants;

	public NbtChecker(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}

	/* general */
	@Override
	public void check_Unbreakable(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("Unbreakable")) {
				throw new NbtNotPermittedException("contains Unbreakable");
			}
		}
	}

	@Override
	public void check_CanDestroy(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("CanDestroy")) {
				throw new NbtNotPermittedException("contains CanDestroy");
			}
		}
	}

	@Override
	public void check_CanPlaceOn(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("CanPlaceOn")) {
				throw new NbtNotPermittedException("contains CanPlaceOn");
			}
		}
	}

	@Override
	public void check_BlockEntityTag(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("BlockEntityTag")) {
				NBTTagCompound blockEntityTag = root.getCompound("BlockEntityTag");

				if (blockEntityTag.hasKey("Lock")) {
					throw new NbtNotPermittedException("contains BlockEntityTag.Lock");
				}

				if (blockEntityTag.hasKey("Text1")
						|| blockEntityTag.hasKey("Text2")
						|| blockEntityTag.hasKey("Text3")
						|| blockEntityTag.hasKey("Text4")) {
					throw new NbtNotPermittedException("contains BlockEntityTag.Text[?1-4]");
				}

				// TODO: only ShulkerBox should have Items (I think?)
				if (blockEntityTag.hasKey("Items")) {
					NBTTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND.getId());

					for (int i = 0; i < items.size(); i++) {
						NBTTagCompound item = items.get(i);

						if (item.hasKey("tag")) {
							// check item tag
						}
					}
				}
			}
		}
	}

	@Override
	public void check_BlockStateTag(Object object) throws NbtNotPermittedException {

	}

	/* enchantments */
	@Override
	public void check_ench(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("ench")) {
				NBTTagList enchantments = root.getList("ench", NbtDataType.COMPOUND.getId());

				for (int i = 0; i < enchantments.size(); i++) {
					Enchantment current = Enchantment.getById(enchantments.get(i).getShort("id"));
					int lvl = 0xFFFF & enchantments.get(i).getShort("lvl");

					if (lvl > current.getMaxLevel()) {
						throw new NbtNotPermittedException("enchantment level too high");
					}

					if (lvl < current.getStartLevel()) {
						throw new NbtNotPermittedException("enchantment level too low");
					}

					for (int j = 0; j < enchantments.size(); j++) {
						Enchantment other = Enchantment.getById(enchantments.get(j).getShort("id"));

						if (current != other && current.conflictsWith(other)) {
							throw new NbtNotPermittedException("conflicting enchantments");
						}
					}
				}
			}
		}
	}

	@Override
	public void check_StoredEnchantments(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_RepairCost(Object object) throws NbtNotPermittedException {

	}

	/* attribute modifiers */
	@Override
	public void check_AttributeModifiers(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("AttributeModifiers")) {
				throw new NbtNotPermittedException("contains AttributeModifiers");
			}
		}
	}

	/* potion effects */
	@Override
	public void check_CustomPotionEffects(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("CustomPotionEffects")) {
				throw new NbtNotPermittedException("contains CustomPotionEffects");
			}
		}
	}

	@Override
	public void check_Potion(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_CustomPotionColor(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			// TODO: can you obtain these in survival?
			if (root.hasKey("CustomPotionColor")) {
				int bgr = root.getInt("CustomPotionColor");

				try {
					Color.fromBGR(bgr);
				} catch (IllegalArgumentException e) {
					throw new NbtNotPermittedException("invalid custom potion color");
				}
			}
		}
	}

	/* display properties */
	@Override
	public void check_display(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("display")) {
				NBTTagCompound display = root.getCompound("display");

				String name = display.getString("Name");

				if (name != null && name.length() > protocolConstants.maxAnvilRenameChars()) {
					throw new NbtNotPermittedException("item name too long");
				}

				NBTTagList lore = display.getList("Lore", NbtDataType.STRING.getId());

				if (lore != null && !lore.isEmpty()) {
					for (int i = 0; i < lore.size(); i++) {
						String line = lore.getString(i);

						if (line != null && line.length() > protocolConstants.NOT_PROTOCOL_maxLoreLineLength()) {
							throw new NbtNotPermittedException("lore line too long");
						}
					}
				}
			}
		}
	}

	@Override
	public void check_HideFlags(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("HideFlags")) {
				throw new NbtNotPermittedException("contains HideFlags");
			}
		}
	}

	/* written books */
	@Override
	public void check_resolved(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_generation(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_author(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_title(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_pages(Object object) throws NbtNotPermittedException {

	}

	/* player heads */
	@Override
	public void check_SkullOwner(Object object) throws NbtNotPermittedException {
		
	}

	/* fireworks */
	@Override
	public void check_Explosion(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_Fireworks(Object object) throws NbtNotPermittedException {
		if (object instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) object;

			if (root.hasKey("Fireworks")) {
				NBTTagCompound fireworks = root.getCompound("Fireworks");

				int flight = fireworks.getInt("Flight");

				if (flight > protocolConstants.fireworksMaxFlight()
						|| flight < protocolConstants.fireworksMinFlight()) {
					throw new NbtNotPermittedException("invalid firework flight time");
				}

				NBTTagList explosions = fireworks.getList("Explosions", NbtDataType.COMPOUND.getId());

				if (explosions != null && explosions.size() > protocolConstants.fireworksMaxExplosions()) {
					throw new NbtNotPermittedException("too many firework effects");
				}
			}
		}
	}

	/* armor stands/spawn eggs/buckets of fish */
	@Override
	public void check_EntityTag(Object object) throws NbtNotPermittedException {

	}

	/* buckets of fish */
	@Override
	public void check_BucketVariantTag(Object object) throws NbtNotPermittedException {

	}

	/* maps */
	@Override
	public void check_map(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_map_scale_direction(Object object) throws NbtNotPermittedException {

	}

	@Override
	public void check_Decorations(Object object) throws NbtNotPermittedException {

	}

	/* suspicious stew */
	@Override
	public void check_Effects(Object object) throws NbtNotPermittedException {

	}

}
