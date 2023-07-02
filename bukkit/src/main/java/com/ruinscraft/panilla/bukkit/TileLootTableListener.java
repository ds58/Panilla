package com.ruinscraft.panilla.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.loot.Lootable;

public class TileLootTableListener implements Listener {

    private boolean checkForLootable;

    public TileLootTableListener() {
        String bVersion = Bukkit.getVersion();

        if (bVersion.contains("1.8") || bVersion.contains("1.12")) {
            checkForLootable = false;
        } else {
            checkForLootable = true;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (checkForLootable) {
            fixLootTable(event.getBlock());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (checkForLootable) {
            fixLootTable(event.getClickedBlock());
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (!checkForLootable) {
            return;
        }

        ItemStack itemStack = event.getItem();

        if (itemStack.getItemMeta() instanceof BlockStateMeta) {
            BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();

            if (blockStateMeta.getBlockState() instanceof ShulkerBox) {
                ShulkerBox shulker = (ShulkerBox) blockStateMeta.getBlockState();

                if (shulker.isPlaced()) {
                    try {
                        if (shulker.getLootTable() != null) {
                            shulker.getLootTable().getKey();
                        }
                    } catch (Exception e) {
                        shulker.setLootTable(null);
                        blockStateMeta.setBlockState(shulker);
                        itemStack.setItemMeta(blockStateMeta);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private static void fixLootTable(Block block) {
        if (block == null) {
            return;
        }

        BlockState blockState = block.getState();

        if (blockState instanceof Lootable) {
            Lootable lootable = (Lootable) blockState;

            try {
                if (lootable.getLootTable() != null) {
                    lootable.getLootTable().getKey();
                }
            } catch (Exception e) {
                lootable.setLootTable(null);
                blockState.update(true);
            }
        }
    }

}
