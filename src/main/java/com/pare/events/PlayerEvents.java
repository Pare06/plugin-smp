package com.pare.events;

import com.pare.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Collections;
import java.util.List;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Main.teleported.remove(event.getEntity().getName());
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        World world = event.getTo().getWorld();

        if (world.getEnvironment() != World.Environment.THE_END
        ||  world.getPlayers().size() > 0
        ||  Main.dragonFight == Main.DragonFight.ACTIVE) {
            return;
        }

        Main.dragonFight = Main.DragonFight.ACTIVE;

        dragonTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            if (Main.dragonFight == Main.DragonFight.INACTIVE) {
                Bukkit.getScheduler().cancelTask(dragonTaskId);
            }

            List<LivingEntity> endermen = world.getLivingEntities().stream().filter(x -> x.getType() == EntityType.ENDERMAN).toList();
            Collections.shuffle(endermen);
            List<LivingEntity> endermen1_3 = endermen.subList(0, Math.round((float)endermen.size() / 3 - 2));

            List<Player> players = world.getPlayers();

            for (var enderman : endermen1_3) {
                Collections.shuffle(players);
                ((Enderman)enderman).setTarget(players.get(0));
            }
        }, 20 * 10, 20 * 30);
    }

    private static int dragonTaskId;
}