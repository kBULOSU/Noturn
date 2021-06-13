package com.noturn.gems.listeners;

import com.noturn.gems.NoturnGemsConstants;
import com.noturn.gems.item.GemItemSupplier;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

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

        int level = EnchantmentManager.getBonusMonsterLootEnchantmentLevel(((CraftPlayer) killer).getHandle());

        int multiplied = NoturnGemsConstants.Config.DEFAULT_DROP_AMOUNT * level;

        ItemStack itemStack = new GemItemSupplier(multiplied).get();

        killer.getWorld().dropItem(event.getEntity().getLocation(), itemStack);
    }
}
