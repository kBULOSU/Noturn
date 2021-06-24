package com.noturn.stackdrops.listeners;

import com.google.common.collect.Maps;
import com.noturn.stackdrops.StackDropsPlugin;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class ItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onMobKill(final EntityDeathEvent e) {
        Map<ItemStack, Integer> toDrop = Maps.newHashMap();
        final Iterator<ItemStack> dropsIt = e.getDrops().iterator();

        while (dropsIt.hasNext()) {
            final ItemStack drop = dropsIt.next();

            boolean added = false;
            for (Map.Entry<ItemStack, Integer> entry : toDrop.entrySet()) {
                if (entry.getKey().isSimilar(drop)) {
                    toDrop.put(entry.getKey(), entry.getValue() + drop.getAmount());
                    added = true;
                    break;
                }
            }

            if (!added) {
                toDrop.put(drop, drop.getAmount());
            }

            dropsIt.remove();
        }

        for (Map.Entry<ItemStack, Integer> entry : toDrop.entrySet()) {
            spawnStack(entry.getKey(), entry.getValue(), e.getEntity(), null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemSpawn(final ItemSpawnEvent e) {
        val item = e.getEntity();
        val itemStack = item.getItemStack();
        if (itemStack.getType() == Material.GOLDEN_APPLE ||
                itemStack.getType() == Material.MOB_SPAWNER) {
            return;
        }

        val cancelEvent = spawnStack(itemStack, itemStack.getAmount(), item, item);

        e.setCancelled(cancelEvent == StackResult.STACKED);
    }


    private StackResult spawnStack(ItemStack itemStack, int itemAmount, Entity source, Item item) {
        for (Entity entity : source.getNearbyEntities(5.0, 5.0, 5.0)) {
            if (!(entity instanceof Item)) {
                continue;
            }

            Item targetItem = (Item) entity;
            if (itemStack.isSimilar(targetItem.getItemStack())) {
                val metadata = targetItem.getMetadata(StackDropsPlugin.METADATA_KEY);
                if (!metadata.isEmpty()) {
                    int amount = metadata.get(0).asInt() + itemAmount;

                    StackDropsPlugin.updateItemSilent(targetItem, amount);

                    targetItem.setTicksLived(2);
                    return StackResult.STACKED;
                }
            }
        }

        if (item == null) {
            item = source.getWorld().dropItemNaturally(source.getLocation(), itemStack);
        }

        StackDropsPlugin.updateItemSilent(item, itemAmount);

        return StackResult.INITIALIZED;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemMerge(final ItemMergeEvent e) {
        val source = e.getEntity();
        val target = e.getTarget();
        if (source.getItemStack().getType() == Material.GOLDEN_APPLE ||
                source.getItemStack().getType() == Material.MOB_SPAWNER) {
            return;
        }

        val sourceMetadata = source.getMetadata(StackDropsPlugin.METADATA_KEY);
        val targetMetadata = target.getMetadata(StackDropsPlugin.METADATA_KEY);
        if (sourceMetadata.isEmpty() && targetMetadata.isEmpty()) {
            return;
        }

        int targetAmount = targetMetadata.isEmpty() ? target.getItemStack().getAmount() : targetMetadata.get(0).asInt();
        int sourceAmount = sourceMetadata.isEmpty() ? source.getItemStack().getAmount() : sourceMetadata.get(0).asInt();

        StackDropsPlugin.updateItemSilent(target, targetAmount + sourceAmount);

        target.setTicksLived(2);
        source.remove();

        e.setCancelled(true);
    }

    private enum StackResult {
        STACKED,
        INITIALIZED
    }
}
