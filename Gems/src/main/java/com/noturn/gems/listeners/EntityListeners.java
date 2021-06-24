package com.noturn.gems.listeners;

import com.noturn.gems.NoturnGemsConstants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityListeners implements Listener {

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (!NoturnGemsConstants.Config.ENTITIES.contains(event.getEntityType())) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (killer == null) {
            return;
        }

        int loot = 0;

        ItemStack itemInHand = killer.getItemInHand();
        if (itemInHand != null && itemInHand.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
            loot = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
        }

        int amount = NoturnGemsConstants.Config.PER_ENTITY_DROPS.getOrDefault(
                event.getEntityType(), NoturnGemsConstants.Config.DEFAULT_DROP_AMOUNT
        );

        if (loot != 0) {
            amount *= loot;
        }

        ItemStack itemStack = new ItemStack(NoturnGemsConstants.Config.ITEM_MATERIAL, NoturnGemsConstants.Config.ITEM_DATA);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(NoturnGemsConstants.Config.ITEM_DISPLAY_NAME);
        itemMeta.setLore(NoturnGemsConstants.Config.ITEM_LORE);

        itemStack.setItemMeta(itemMeta);

        itemStack.setAmount(amount);

        killer.getWorld().dropItemNaturally(event.getEntity().getLocation(), itemStack);
    }
}
