package com.pare.commands;

import com.pare.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class Continent implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("/continent <europa/africa/asia/america>");
            return true;
        }

        int coordX, coordY, coordZ;
        Random rand = new Random();
        Player player = (Player) commandSender;
        Material blockAtI = null;

        do {
            switch (strings[0]) {
                case "africa" -> {
                    coordX = rand.nextInt(-800, 2200);
                    coordZ = rand.nextInt(-2300, 2300);
                }
                case "europa" -> {
                    coordX = rand.nextInt(-750, 2500);
                    coordZ = rand.nextInt(-4700, -2500);
                }
                case "asia" -> {
                    coordX = rand.nextInt(2500, 10000);
                    coordZ = rand.nextInt(-4400, 3000);
                }
                case "america" -> {
                    coordX = rand.nextInt(-8400, -3000);
                    coordZ = rand.nextInt(-4500, 3000);
                }
                default -> {
                    commandSender.sendMessage("/continent <europa/africa/asia/america>");
                    return true;
                }
            }

            coordY = 257;
            for (int i = 255; i > 0; i--) {
                blockAtI = player.getWorld().getBlockAt(coordX, i, coordZ).getType();
                if (blockAtI != Material.AIR) {
                    coordY = i + 2;
                    break;
                }
            }
        } while (blockAtI == Material.WATER || blockAtI == Material.LAVA || coordY == 257 || blockAtI == null);

        Location loc = new Location(player.getWorld(), coordX, coordY, coordZ);

        player.teleport(loc);
        Main.teleported.add(player.getName());
        return true;
    }
}
