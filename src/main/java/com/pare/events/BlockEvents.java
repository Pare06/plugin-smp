package com.pare.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEvents implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SCULK_CATALYST) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
        }
    }
}
