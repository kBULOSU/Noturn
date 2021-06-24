package com.noturn.stackdrops.packet;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class CollectItemAnimation {

    public CollectItemAnimation(final Player player, final Item item) {
        EntityItem entityItem = new EntityItem(
                ((CraftWorld) player.getWorld()).getHandle(),
                item.getLocation().getX(),
                item.getLocation().getY(),
                item.getLocation().getZ(),
                CraftItemStack.asNMSCopy(item.getItemStack())
        );

        PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(entityItem, 2, 100);

        PacketPlayOutEntityMetadata packetPlayOutEntityMetadata = new PacketPlayOutEntityMetadata(
                entityItem.getId(), entityItem.getDataWatcher(), true
        );

        PacketPlayOutCollect packetPlayOutCollect = new PacketPlayOutCollect(entityItem.getId(), player.getEntityId());

        Location loc = player.getLocation();

        for (Player target : player.getWorld().getPlayers()) {
            if (loc.distanceSquared(target.getLocation()) < 100) {

                PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;

                connection.sendPacket(packetPlayOutSpawnEntity);
                connection.sendPacket(packetPlayOutEntityMetadata);
                connection.sendPacket(packetPlayOutCollect);
            }
        }
    }
}