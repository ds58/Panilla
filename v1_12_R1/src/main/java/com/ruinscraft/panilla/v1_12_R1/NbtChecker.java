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
	public void check_Unbreakable(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("Unbreakable")) {
				throw new NbtNotPermittedException("Unbreakable");
			}
		}
	}

	@Override
	public void check_CanDestroy(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CanDestroy")) {
				throw new NbtNotPermittedException("CanDestroy");
			}
		}
	}

	@Override
	public void check_CanPlaceOn(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CanPlaceOn")) {
				throw new NbtNotPermittedException("CanPlaceOn");
			}
		}
	}

	@Override
	public void check_BlockEntityTag(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("BlockEntityTag")) {
				NBTTagCompound blockEntityTag = root.getCompound("BlockEntityTag");

				if (blockEntityTag.hasKey("Lock")) {
					throw new NbtNotPermittedException("BlockEntityTag");
				}

				if (blockEntityTag.hasKey("Text1")
						|| blockEntityTag.hasKey("Text2")
						|| blockEntityTag.hasKey("Text3")
						|| blockEntityTag.hasKey("Text4")) {
					throw new NbtNotPermittedException("BlockEntityTag");
				}

				// TODO: only ShulkerBox should have Items (I think?)
				if (blockEntityTag.hasKey("Items")) {
					NBTTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND.getId());

					for (int i = 0; i < items.size(); i++) {
						NBTTagCompound item = items.get(i);

						if (item.hasKey("tag")) {
							// check item _tag
						}
					}
				}
			}
		}
	}

	@Override
	public void check_BlockStateTag(Object _tag) throws NbtNotPermittedException {

	}

	/* enchantments */
	@Override
	public void check_ench(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("ench") || root.hasKey("Enchantments")) {
				String using = "ench";

				NBTTagList enchantments = root.getList("ench", NbtDataType.COMPOUND.getId());

				if (enchantments == null) {
					enchantments = root.getList("Enchantments", NbtDataType.COMPOUND.getId());
					using = "Enchantments";
				}

				for (int i = 0; i < enchantments.size(); i++) {
					Enchantment current = Enchantment.getById(enchantments.get(i).getShort("id"));
					int lvl = 0xFFFF & enchantments.get(i).getShort("lvl");

					if (lvl > current.getMaxLevel()) {
						throw new NbtNotPermittedException(using);
					}

					if (lvl < current.getStartLevel()) {
						throw new NbtNotPermittedException(using);
					}

					for (int j = 0; j < enchantments.size(); j++) {
						Enchantment other = Enchantment.getById(enchantments.get(j).getShort("id"));

						if (current != other && current.conflictsWith(other)) {
							throw new NbtNotPermittedException(using);
						}
					}
				}
			}
		}
	}

	@Override
	public void check_StoredEnchantments(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_RepairCost(Object _tag) throws NbtNotPermittedException {

	}

	/* attribute modifiers */
	@Override
	public void check_AttributeModifiers(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("AttributeModifiers")) {
				throw new NbtNotPermittedException("AttributeModifiers");
			}
		}
	}

	/* potion effects */
	@Override
	public void check_CustomPotionEffects(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CustomPotionEffects")) {
				throw new NbtNotPermittedException("CustomPotionEffects");
			}
		}
	}

	@Override
	public void check_Potion(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_CustomPotionColor(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			// TODO: can you obtain these in survival?
			if (root.hasKey("CustomPotionColor")) {
				int bgr = root.getInt("CustomPotionColor");

				try {
					Color.fromBGR(bgr);
				} catch (IllegalArgumentException e) {
					throw new NbtNotPermittedException("CustomPotionColor");
				}
			}
		}
	}

	/* display properties */
	@Override
	public void check_display(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("display")) {
				NBTTagCompound display = root.getCompound("display");

				String name = display.getString("Name");

				if (name != null && name.length() > protocolConstants.maxAnvilRenameChars()) {
					throw new NbtNotPermittedException("display");
				}

				NBTTagList lore = display.getList("Lore", NbtDataType.STRING.getId());

				if (lore != null && !lore.isEmpty()) {
					for (int i = 0; i < lore.size(); i++) {
						String line = lore.getString(i);

						if (line != null && line.length() > protocolConstants.NOT_PROTOCOL_maxLoreLineLength()) {
							throw new NbtNotPermittedException("display");
						}
					}
				}
			}
		}
	}

	@Override
	public void check_HideFlags(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("HideFlags")) {
				throw new NbtNotPermittedException("HideFlags");
			}
		}
	}

	/* written books */
	@Override
	public void check_resolved(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_generation(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_author(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_title(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_pages(Object _tag) throws NbtNotPermittedException {

	}

	/* player heads */
	@Override
	public void check_SkullOwner(Object _tag) throws NbtNotPermittedException {

	}

	/* fireworks */
	@Override
	public void check_Explosion(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_Fireworks(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("Fireworks")) {
				NBTTagCompound fireworks = root.getCompound("Fireworks");

				int flight = fireworks.getInt("Flight");

				if (flight > protocolConstants.fireworksMaxFlight()
						|| flight < protocolConstants.fireworksMinFlight()) {
					throw new NbtNotPermittedException("Fireworks");
				}

				NBTTagList explosions = fireworks.getList("Explosions", NbtDataType.COMPOUND.getId());

				if (explosions != null && explosions.size() > protocolConstants.fireworksMaxExplosions()) {
					throw new NbtNotPermittedException("Fireworks");
				}
			}
		}
	}

	/* armor stands/spawn eggs/buckets of fish */
	@Override
	public void check_EntityTag(Object _tag) throws NbtNotPermittedException {
	}

	/* buckets of fish */
	@Override
	public void check_BucketVariantTag(Object _tag) throws NbtNotPermittedException {

	}

	/* maps */
	@Override
	public void check_map(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_map_scale_direction(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void check_Decorations(Object _tag) throws NbtNotPermittedException {

	}

	/* suspicious stew */
	@Override
	public void check_Effects(Object _tag) throws NbtNotPermittedException {

	}

	@Override
	public void checkForNotValid(Object _tag) throws NbtNotPermittedException {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			for (String subTag : root.c()) {
				try {
					final String methodName = "check_" + subTag;
					getClass().getMethod(methodName, Object.class);
				} catch (NoSuchMethodException e) {
					throw new NbtNotPermittedException(subTag);
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
