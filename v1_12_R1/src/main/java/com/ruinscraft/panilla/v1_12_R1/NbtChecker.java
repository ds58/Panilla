package com.ruinscraft.panilla.v1_12_R1;

import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;

import com.ruinscraft.panilla.api.INbtChecker;
import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.NbtDataType;
import com.ruinscraft.panilla.api.config.PStrictness;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class NbtChecker implements INbtChecker {

	private final IProtocolConstants protocolConstants;

	public NbtChecker(IProtocolConstants protocolConstants) {
		this.protocolConstants = protocolConstants;
	}

	/* general */
	@Override
	public boolean check_Unbreakable(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("Unbreakable")) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean check_CanDestroy(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CanDestroy")) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean check_CanPlaceOn(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CanPlaceOn")) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean check_BlockEntityTag(Object _tag, PStrictness strictness) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("BlockEntityTag")) {
				NBTTagCompound blockEntityTag = root.getCompound("BlockEntityTag");

				if (blockEntityTag.hasKey("Lock")) {
					return false;
				}

				if (blockEntityTag.hasKey("Text1")
						|| blockEntityTag.hasKey("Text2")
						|| blockEntityTag.hasKey("Text3")
						|| blockEntityTag.hasKey("Text4")) {
					return false;
				}

				// TODO: only ShulkerBox should have Items (I think?)
				if (blockEntityTag.hasKey("Items")) {
					NBTTagList items = blockEntityTag.getList("Items", NbtDataType.COMPOUND.getId());

					for (int i = 0; i < items.size(); i++) {
						NBTTagCompound item = items.get(i);

						if (item.hasKey("tag")) {
							checkAll(_tag, strictness);	// recursive
						}
					}
				}
			}
		}

		return true;
	}

	// 1.14
	@Override
	public boolean check_BlockStateTag(Object _tag) {
		return true;
	}

	/* enchantments */
	@Override
	public boolean check_ench(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("ench") || root.hasKey("Enchantments") || root.hasKey("StoredEnchantments")) {
				NBTTagList enchantments = root.getList("ench", NbtDataType.COMPOUND.getId());

				if (enchantments == null) {
					enchantments = root.getList("Enchantments", NbtDataType.COMPOUND.getId());
				}

				if (enchantments == null) {
					enchantments = root.getList("StoredEnchantments", NbtDataType.COMPOUND.getId());
				}

				for (int i = 0; i < enchantments.size(); i++) {
					Enchantment current = Enchantment.getById(enchantments.get(i).getShort("id"));
					int lvl = 0xFFFF & enchantments.get(i).getShort("lvl");

					if (lvl > current.getMaxLevel()) {
						return false;
					}

					if (lvl < current.getStartLevel()) {
						return false;
					}

					for (int j = 0; j < enchantments.size(); j++) {
						Enchantment other = Enchantment.getById(enchantments.get(j).getShort("id"));

						if (current != other && current.conflictsWith(other)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean check_RepairCost(Object _tag) {
		return true;
	}

	/* attribute modifiers */
	@Override
	public boolean check_AttributeModifiers(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("AttributeModifiers")) {
				return false;
			}
		}

		return true;
	}

	/* potion effects */
	@Override
	public boolean check_CustomPotionEffects(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("CustomPotionEffects")) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean check_Potion(Object _tag) {
		return true;
	}

	@Override
	public boolean check_CustomPotionColor(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			// TODO: can you obtain these in survival?
			if (root.hasKey("CustomPotionColor")) {
				int bgr = root.getInt("CustomPotionColor");

				try {
					Color.fromBGR(bgr);
				} catch (IllegalArgumentException e) {
					return false;
				}
			}
		}

		return true;
	}

	/* display properties */
	@Override
	public boolean check_display(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("display")) {
				NBTTagCompound display = root.getCompound("display");

				String name = display.getString("Name");

				if (name != null && name.length() > protocolConstants.maxAnvilRenameChars()) {
					return false;
				}

				NBTTagList lore = display.getList("Lore", NbtDataType.STRING.getId());

				if (lore != null && !lore.isEmpty()) {
					for (int i = 0; i < lore.size(); i++) {
						String line = lore.getString(i);

						if (line != null && line.length() > protocolConstants.NOT_PROTOCOL_maxLoreLineLength()) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean check_HideFlags(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("HideFlags")) {
				return false;
			}
		}

		return true;
	}

	/* written books */
	@Override
	public boolean check_resolved(Object _tag) {
		return true;
	}

	@Override
	public boolean check_generation(Object _tag) {
		return true;
	}

	@Override
	public boolean check_author(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("author")) {
				int authorLength = root.getString("author").length();

				if (authorLength > protocolConstants.maxUsernameLength()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean check_title(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("title")) {
				int titleLength = root.getString("title").length();

				if (titleLength > protocolConstants.maxBookTitleLength()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean check_pages(Object _tag) {
		return true;	// TODO: can books be forced to have more than 50 pages?
	}

	/* player heads */
	@Override
	public boolean check_SkullOwner(Object _tag) {
		return true;	// TODO: have crash skulls been patched?
	}

	/* fireworks */
	@Override
	public boolean check_Explosion(Object _tag) {
		return true;	// shouldn't need to worry about this one
	}

	@Override
	public boolean check_Fireworks(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			if (root.hasKey("Fireworks")) {
				NBTTagCompound fireworks = root.getCompound("Fireworks");

				int flight = fireworks.getInt("Flight");

				if (flight > protocolConstants.maxFireworksFlight()
						|| flight < protocolConstants.minFireworksFlight()) {
					return false;
				}

				NBTTagList explosions = fireworks.getList("Explosions", NbtDataType.COMPOUND.getId());

				if (explosions != null && explosions.size() > protocolConstants.maxFireworksExplosions()) {
					return false;
				}
			}
		}

		return true;
	}

	/* armor stands/spawn eggs/buckets of fish */
	@Override
	public boolean check_EntityTag(Object _tag) {
		return true;
	}

	/* buckets of fish */
	@Override
	public boolean check_BucketVariantTag(Object _tag) {
		return true;
	}

	/* maps */
	@Override
	public boolean check_map(Object _tag) {
		return true;
	}

	@Override
	public boolean check_map_scale_direction(Object _tag) {
		return true;
	}

	@Override
	public boolean check_Decorations(Object _tag) {
		return true;
	}

	/* suspicious stew (1.14) */
	@Override
	public boolean check_Effects(Object _tag) {
		return true;
	}

	@Override
	public String checkForNotValid(Object _tag) {
		if (_tag instanceof NBTTagCompound) {
			NBTTagCompound root = (NBTTagCompound) _tag;

			for (String subTag : root.c()) {
				try {
					final String methodName = "check_" + subTag;
					getClass().getMethod(methodName, Object.class);
				} catch (NoSuchMethodException e) {
					return subTag;
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

}
