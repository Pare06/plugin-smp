package com.pare.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.pare.Helpers.*;

public class HeartEvents implements Listener {
    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || !event.hasItem()) {
            return;
        }

        ItemStack item = event.getItem();

        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        Player player = event.getPlayer();

        if (meta.getDisplayName().equals("Cuore") && meta.hasEnchant(Enchantment.BINDING_CURSE) && item.getType() == Material.APPLE) {
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth.getBaseValue() >= 40) {
                player.sendMessage(ChatColor.DARK_RED + "Hai raggiunto il limite massimo di vita!");
                return;
            }

            maxHealth.setBaseValue(maxHealth.getBaseValue() + 2);
            player.getInventory().remove(item);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe() == heartRecipe) {
            event.setCurrentItem(heartItem);
        }
        if (event.getRecipe() == appleRecipe) {
            event.setCurrentItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        }
    }
}
