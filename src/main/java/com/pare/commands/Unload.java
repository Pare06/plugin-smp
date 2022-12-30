package com.pare.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Unload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage("NO!");
        }

        if (strings.length == 0) {
            return false;
        }

        World world;
        boolean save;
        switch (strings[0]) {
            case "nether":
                world = Bukkit.getWorld("world_nether");
                save = true;
                break;
            case "end":
                world = Bukkit.getWorld("world_the_end");
                save = false;
                break;
            default:
                return false;
        }

        if (world.getPlayers().size() > 0) {
            commandSender.sendMessage("C'è almeno un player in quel mondo!");
        } else {
            Bukkit.unloadWorld(world, save);
        }

        return true;
    }
}
