package com.noturn.gems.commands;

import com.noturn.gems.NoturnGemsConstants;
import com.noturn.gems.NoturnGemsPlugin;
import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.misc.utils.NumberUtil;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.Execution;
import me.saiintbrisson.minecraft.command.annotations.Command;
import me.saiintbrisson.minecraft.command.annotations.CommandTarget;
import me.saiintbrisson.minecraft.command.argument.Argument;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectTargetException;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class GemsCommand {

    private final GemsUserController userController;

    @Command(
            name = "gems",
            usage = "gems <jogador>",
            aliases = {"gemas"}
    )
    public void cashCommand(Execution execution, @Argument(nullable = true) String player) {
        if (player == null) {

            if (!execution.isPlayer()) {
                throw new IncorrectTargetException(CommandTarget.PLAYER);
            }

            String formatted = NumberUtil.toK(userController.get(execution.getPlayer().getName()));

            execution.sendMessage("§eVocê possui §f" + formatted + " §egemas.");

            return;
        }

        String format = String.format(
                "§f%s §epossui §f%s §egemas!",
                player,
                NumberUtil.toK(userController.get(player))
        );

        execution.sendMessage(format);
    }

    @Command(
            name = "gems.give",
            usage = "gems dar <jogador> <quantia>",
            aliases = {"dar", "add"},
            permission = "noturngems.admin"
    )
    public void giveCommand(Execution execution, String player, double amount) {
        Double cached = userController.get(player);
        if (cached == null) {
            execution.sendMessage("§cUsuário inválido.");
            return;
        }

        userController.merge(player, amount, true);

        execution.sendMessage(
                "§eVocê adicionou §f%s §egemas para §f%s§e!",
                NumberUtil.toK(amount),
                player
        );
    }

    @Command(
            name = "gems.remove",
            usage = "gems remover <jogador> <quantia>",
            aliases = {"remover", "retirar"},
            permission = "noturngems.admin"
    )
    public void removeCommand(Execution execution, String player, double amount) {
        Double cached = userController.get(player);
        if (cached == null) {
            execution.sendMessage("§cUsuário inválido.");
            return;
        }

        userController.merge(player, amount, false);

        execution.sendMessage(
                "§eVocê removeu §f%s §egemas de §f%s§e!",
                NumberUtil.toK(amount),
                player
        );
    }

    @Command(
            name = "gems.set",
            aliases = {"definir", "setar"},
            usage = "gems definir <jogador> <quantia>",
            permission = "noturngems.admin"
    )
    public void defineCommand(Execution execution, String player, double amount) {
        userController.put(player, amount);

        execution.sendMessage(
                "§eVocê definiu §f%s §egemas para §f%s§e!",
                NumberUtil.toK(amount),
                player
        );
    }

    @Command(
            name = "gems.sell",
            aliases = {"vender"},
            usage = "gems vender",
            permission = "noturngems.vender"
    )
    public void sellCommand(Execution execution) {
        if (!execution.isPlayer()) {
            throw new IncorrectTargetException(CommandTarget.PLAYER);
        }

        Player player = execution.getPlayer();

        int gemAmount = 0;

        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null || content.getType() != NoturnGemsConstants.Config.ITEM_MATERIAL) {
                continue;
            }

            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(content);
            if (!itemStack.hasTag() || !itemStack.getTag().getBoolean("gem")) {
                continue;
            }

            gemAmount += content.getAmount();

            player.getInventory().remove(content);
        }

        if (gemAmount == 0) {
            execution.sendMessage("§cNão foram encontradas gemas para vender.");
            return;
        }

        double sellAmount = gemAmount * getUserMultiplier(player);
        double sellValue = sellAmount * NoturnGemsConstants.Config.DEFAULT_SELL_VALUE;

        NoturnGemsPlugin.INSTANCE.getEconomy().depositPlayer(player.getName(), sellValue);

        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);

        execution.sendMessage(String.format(
                "§eVocê vendeu §a%s §egemas por §a%s §ecoins!",
                NumberUtil.toK(sellAmount),
                NumberUtil.toK(sellValue)
        ));
    }

    private double getUserMultiplier(Player player) {

        String prefix = "gemas.vender.";

        for (Map.Entry<String, Double> entry : NoturnGemsConstants.Config.MULTIPLIERS.entrySet()) {
            if (player.hasPermission(prefix + entry.getKey())) {
                return entry.getValue();
            }
        }

        return 1.0;
    }
}
