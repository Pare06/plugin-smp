package com.pare.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Exit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            Bukkit.getConsoleSender().sendMessage("save-all");
            Bukkit.getConsoleSender().sendMessage("stop");
        }
        return true;
    }
}
