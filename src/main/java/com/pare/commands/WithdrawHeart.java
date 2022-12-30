package com.pare.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.pare.Helpers.heartItem;

public class WithdrawHeart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (maxHealth.getBaseValue() > 20) {
            maxHealth.setBaseValue(maxHealth.getBaseValue() - 2);
            player.getInventory().addItem(heartItem);
        } else {
            player.sendMessage("Devi avere dei cuori bonus!");
        }

        return true;
    }
}
