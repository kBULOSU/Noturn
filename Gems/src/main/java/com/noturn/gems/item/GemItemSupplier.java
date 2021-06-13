package com.noturn.gems.item;

import com.noturn.gems.NoturnGemsConstants;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class GemItemSupplier implements Supplier<ItemStack> {

    private final int amount;

    @Override
    public ItemStack get() {
        ItemStack item = new ItemStack(NoturnGemsConstants.Config.ITEM_MATERIAL, amount, NoturnGemsConstants.Config.ITEM_DATA);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(NoturnGemsConstants.Config.ITEM_DISPLAY_NAME);
        itemMeta.setLore(NoturnGemsConstants.Config.ITEM_LORE);

        item.setItemMeta(itemMeta);

        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("gem", true);

        itemStack.setTag(tag);

        ItemMeta nmsItemMeta = CraftItemStack.asBukkitCopy(itemStack).getItemMeta();

        item.setItemMeta(nmsItemMeta);

        return item;
    }
}
