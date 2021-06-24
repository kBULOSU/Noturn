package com.noturn.stackdrops.listeners;

import com.noturn.stackdrops.StackDropsPlugin;
import com.noturn.stackdrops.packet.CollectItemAnimation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class ItemPickupListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPickup(final PlayerPickupItemEvent e) {
        e.setCancelled(
                performPickup(
                        e.getItem(),
                        e.getPlayer().getInventory(),
                        e.getPlayer()
                )
        );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void onHopperPickup(final InventoryPickupItemEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.HOPPER))
            return;

        e.setCancelled(
                performPickup(
                        e.getItem(),
                        e.getInventory(),
                        null
                )
        );
    }

    private boolean performPickup(final Item item, final Inventory inventory, final Player player) {
        val metadata = item.getMetadata(StackDropsPlugin.METADATA_KEY);
        if (metadata.isEmpty()) {
            return false;
        }

        val amount = metadata.get(0).asInt();
        val leftOver = addItemToInv(item.getItemStack(), amount, inventory);

        if (player != null)
            collectItem(player, item);
        if (leftOver == 0) {
            item.remove();
        } else {
            StackDropsPlugin.updateItemSilent(item, leftOver);
        }

        return true;
    }

    private int addItemToInv(final ItemStack itemStack, final int amount, final Inventory inventory) {
        itemStack.setAmount(amount);

        Map<Integer, ItemStack> result = inventory.addItem(itemStack);
        if (!result.isEmpty()) {
            return result.values().iterator().next().getAmount();
        }

        return 0;
    }

    private void collectItem(final Player player, final Item item) {
        new CollectItemAnimation(player, item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5F, 10F);
    }
}
