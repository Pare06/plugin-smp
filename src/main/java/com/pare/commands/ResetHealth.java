package com.pare.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetHealth implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            ((Player)commandSender).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        } else {
            commandSender.sendMessage("NO");
        }

        return true;
    }
}
