package com.noturn.gems.listeners.item;

import com.noturn.gems.NoturnGemsConstants;
import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.dao.GemsUserDAO;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@RequiredArgsConstructor
public class GemItemListener implements Listener {

    private final GemsUserController userController;
    private final GemsUserDAO userDAO;

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null || itemInHand.getType() != NoturnGemsConstants.Config.ITEM_MATERIAL) {
            return;
        }

        if (!itemInHand.isSimilar(NoturnGemsConstants.Config.ITEM)) {
            return;
        }

        int amount = itemInHand.getAmount();

        if (amount > 1) {
            itemInHand.setAmount(amount - 1);
            player.setItemInHand(itemInHand);
        } else {
            itemInHand.setAmount(0);
            itemInHand.setType(Material.AIR);
            itemInHand.setData(new MaterialData(Material.AIR));
            itemInHand.setItemMeta(null);
            player.setItemInHand(new ItemStack(Material.AIR));
        }

        Double merge = userController.merge(player.getName(), 1, true);
        userDAO.insertOrUpdate(player.getName(), merge);
    }
}
