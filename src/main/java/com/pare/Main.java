package com.pare;

import com.pare.commands.*;
import com.pare.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    public static JavaPlugin plugin;
    public static ArrayList<String> teleported;

    public static DragonFight dragonFight;
    public static boolean hasZombieSpawned;

    public enum DragonFight {
        ACTIVE,
        INACTIVE
    }

    @Override
    public void onEnable() {
        plugin = this;
        teleported = new ArrayList<>();

        dragonFight = DragonFight.INACTIVE;
        hasZombieSpawned = false;

        Helpers.registerCraftingRecipes();

        getServer().getPluginManager().registerEvents(new MobEvents(), this);
        getServer().getPluginManager().registerEvents(new HeartEvents(), this);
        getServer().getPluginManager().registerEvents(new BlockEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getCommand("continent").setExecutor(new Continent());
        getCommand("resethealth").setExecutor(new ResetHealth());
        getCommand("withdrawheart").setExecutor(new WithdrawHeart());
        getCommand("collect").setExecutor(new Collect());
        getCommand("exit").setExecutor(new Exit());
        // getCommand("unload").setExecutor(new Unload());

        // Bukkit.unloadWorld("world_the_end", true);
    }

    @Override
    public void onDisable() {

    }
}
