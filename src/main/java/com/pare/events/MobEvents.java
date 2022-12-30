package com.pare.events;

import com.pare.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Random;

public class MobEvents implements Listener {
    @EventHandler
    public void onWardenSpawn(CreatureSpawnEvent event) {
        LivingEntity mob = event.getEntity();

        if (mob.getType() != EntityType.WARDEN) {
            return;
        }

        for (LivingEntity entity : event.getLocation().getWorld().getLivingEntities()) {
            if (entity.getType() == EntityType.WARDEN) {
                event.setCancelled(true);
            }
        }

        AttributeInstance attribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(attribute.getBaseValue() * 2);
        mob.setHealth(mob.getHealth() * 2);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.playSound(pl.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, (float) 0.5F, (float)0.5);
        }

        BossBar bar = Bukkit.createBossBar("Warden", BarColor.RED, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
        bar.setVisible(true);

        int x, y, z;
        x = event.getLocation().getBlockX();
        y = event.getLocation().getBlockY();
        z = event.getLocation().getBlockZ();

        for (Player pl : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(pl);
            pl.sendMessage(String.format("Un warden è spawnato a x: %s, y: %s, z: %s!", x, y, z));
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            bar.setProgress(mob.getHealth() / attribute.getBaseValue());
            if (mob.getHealth() == 0) {
                bar.removeAll();
            }
        }, 0, 10);
    }

    private int witherTaskId;

    @EventHandler
    public void onWitherSpawn(CreatureSpawnEvent event) {
        LivingEntity mob = event.getEntity();
        World world = mob.getWorld();

        if (mob.getType() != EntityType.WITHER) {
            return;
        }

        for (World wrld : Bukkit.getWorlds()) {
            for (LivingEntity ent : wrld.getLivingEntities()) {
                if (ent.getType() == EntityType.WITHER) {
                    event.setCancelled(true);
                }
            }
        }

        AttributeInstance attribute = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(attribute.getBaseValue() * 1.5);
        mob.setHealth(mob.getHealth() * 1.5);

        witherTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            if (mob.getHealth() == 0) {
                Bukkit.getScheduler().cancelTask(witherTaskId);
            }

            Random random = new Random();
            for (int i = 0; i < random.nextInt(6, 9); i++) {
                Mob skeleton = (Mob) world.spawnEntity(mob.getLocation(), EntityType.WITHER_SKELETON);
                skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20 * 0.75);
            }
        }, 20 * 10, 20 * 15);
    }

    @EventHandler
    public void onWardenDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.WARDEN) {
            event.setDamage(event.getDamage() / 2);
        }
    }

    @EventHandler
    public void onWardenArrow(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.WARDEN && event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            event.setDamage(event.getDamage() / 3);
        }
    }

    @EventHandler
    public void onEndLoadEvent(PlayerPortalEvent event) {
        if (event.getTo().getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        if (event.getTo().getWorld().getPlayers().size() == 0) {
            EnderDragon dragon = event.getTo().getWorld().getEnderDragonBattle().getEnderDragon();

            AttributeInstance attribute = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(600);
            dragon.setHealth(600);
        }
    }

    @EventHandler
    public void onCrystalKill(EntityDamageEvent event) {
        Location deathPos = event.getEntity().getLocation();
        if (deathPos.getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> deathPos.getWorld().spawnEntity(deathPos, EntityType.ENDER_CRYSTAL), 20 * 60);
        }
    }

    @EventHandler
    public void onWardenFall(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.WARDEN) {
            switch (event.getCause()) {
                case FALL, FALLING_BLOCK, CRAMMING, DROWNING, FIRE -> event.setCancelled(true);
            }
        }
    }
}
