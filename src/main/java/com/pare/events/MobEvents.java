package com.pare.events;

import com.pare.Main;
import org.bukkit.*;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        attribute.setBaseValue(attribute.getBaseValue() * 3);
        mob.setHealth(mob.getHealth() * 3);

        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.playSound(pl.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 0.5F, 0.5F);
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

        wardenTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            List<Entity> _nearPlayers = mob.getNearbyEntities(30, 10, 30).stream().filter(p -> p.getType() == EntityType.PLAYER).toList();
            List<Entity> nearPlayers = new ArrayList<>(_nearPlayers);
            Collections.shuffle(nearPlayers);

            if (nearPlayers.size() == 0) {
                nearPlayers = mob.getNearbyEntities(200, 200, 200).stream().filter(p -> p.getType() == EntityType.PLAYER).toList();
            }

            for (var pl : nearPlayers) {
                ((Warden)mob).setAnger(pl, 0);
            }
            ((Warden)mob).setAnger(nearPlayers.get(0), 100);

            if (mob.isDead()) {
                Bukkit.getScheduler().cancelTask(wardenTaskId);
            }
        }, 10 * 20, 10 * 20);
    }

    private int witherTaskId, wardenTaskId;

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

    private int dragonHPTaskId;

    @EventHandler
    public void onEndLoadEvent(PlayerPortalEvent event) {
        if (event.getTo().getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        if (event.getTo().getWorld().getPlayers().size() == 0) {
            EnderDragon dragon = event.getTo().getWorld().getEnderDragonBattle().getEnderDragon();

            AttributeInstance attribute = dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attribute.setBaseValue(1000);
            dragon.setHealth(1000);
        }

        dragonHPTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            EnderDragon dragon = event.getTo().getWorld().getEnderDragonBattle().getEnderDragon();

            if (!Main.hasZombieSpawned && dragon.getHealth() <= 500) {
                Zombie zombie = (Zombie)Bukkit.getWorld("world_the_end").spawnEntity(
                    new Location(Bukkit.getWorld("world_the_end"), 0, 100, 0 ), EntityType.ZOMBIE
                );

                Main.hasZombieSpawned = true;
                zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
                zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 2);
                zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(1000);
                zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
                zombie.setHealth(100);
                zombie.setCustomName("Ender Guardian");

                EntityEquipment equip = zombie.getEquipment();

                equip.setHelmet(new ItemStack(Material.OBSIDIAN));
                equip.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
                equip.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
                equip.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

                zombie.setMetadata("custom_mob", new FixedMetadataValue(Main.plugin, true));

                BossBar bar = Bukkit.createBossBar("Ender Guardian", BarColor.RED, BarStyle.SOLID);
                bar.setVisible(true);
                bar.setProgress(zombie.getHealth() / zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                for (var pl : Bukkit.getOnlinePlayers()) {
                    bar.addPlayer(pl);
                }

                zombieBarTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
                    bar.setProgress(zombie.getHealth() / zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    for (var pl : Bukkit.getOnlinePlayers()) {
                        bar.addPlayer(pl);
                    }
                    if (zombie.isDead()) {
                        bar.removeAll();
                        Bukkit.getScheduler().cancelTask(zombieBarTaskId);
                    }
                }, 0, 10);

                Bukkit.getScheduler().cancelTask(dragonHPTaskId);
            }
        }, 0, 1 * 20);
    }

    private int zombieBarTaskId;

    @EventHandler
    public void onCrystalKill(EntityDamageEvent event) {
        Location deathPos = event.getEntity().getLocation();
        if (deathPos.getWorld().getEnvironment() != World.Environment.THE_END) {
            return;
        }

        if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
                Bukkit.getWorld("world_the_end").spawnEntity(deathPos, EntityType.LIGHTNING);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
                    deathPos.getWorld().spawnEntity(deathPos, EntityType.ENDER_CRYSTAL);
                }, 20 * 1);
            }, 20 * 60);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        switch (event.getEntityType()) {
            case WARDEN:
                switch (event.getCause()) {
                    case FALL, FALLING_BLOCK, CRAMMING, DROWNING, FIRE -> event.setCancelled(true);
                }
                break;
            case ENDER_DRAGON:
                switch (event.getCause()) {
                    case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> event.setCancelled(true);
                }
                break;
            case ZOMBIE:
                if (event.getEntity().hasMetadata("custom_mob")) {
                    switch (event.getCause()) {
                        case FALL -> event.setCancelled(true);
                    }
                }
                break;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.ENDER_DRAGON) {
            Main.dragonFight = Main.DragonFight.INACTIVE;

            for (var player : Bukkit.getWorld("world_the_end").getPlayers()) {
                player.performCommand("music stop");
            }
        }
    }
}
