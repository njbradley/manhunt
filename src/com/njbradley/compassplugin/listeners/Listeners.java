package com.njbradley.compassplugin.listeners;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.njbradley.compassplugin.Main;
import com.njbradley.compassplugin.PlayerData;
import com.njbradley.compassplugin.HunterData;

public class Listeners implements Listener {
    Main plugin;

    public Listeners(Main main) {
        plugin  = main;
    }

    @EventHandler
    void event(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        PlayerData data = null;
        for (PlayerData rd : plugin.runners) {
            if (rd.player == player) {
                data = rd;
            }
        }
        for (PlayerData hd : plugin.hunters) {
            if (hd.player == player) {
                data = hd;
            }
        }
        if (data != null) {
            data.updateLastLocation(event.getFrom());
            data.updateFirstLocation(event.getTo());
        }
    }

    @EventHandler
    void event(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = null;
        for (PlayerData rd : plugin.runners) {
            if (rd.player.getDisplayName().equals(player.getDisplayName())) {
                data = rd;
                break;
            }
        }
        for (PlayerData hd : plugin.hunters) {
            if (hd.player.getDisplayName().equals(player.getDisplayName())) {
                data = hd;
                break;
            }
        }
        if (data != null) {
            data.player = player;
        }
    }

    @EventHandler
    void event(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData data = null;
        for (PlayerData rd : plugin.runners) {
            if (rd.player == player) {
                data = rd;
                break;
            }
        }
        for (PlayerData hd : plugin.hunters) {
            if (hd.player == player) {
                data = hd;
                List<ItemStack> drops = event.getDrops();
                for (ItemStack item : drops) {
                    if (item.getType() == Material.COMPASS) {
                        item.setAmount(0);
                    }
                }
                break;
            }
        }
        if (data != null) {
            data.lastpos_overworld = null;
            data.firstpos_overworld = null;
            data.lastpos_nether = null;
            data.firstpos_nether = null;
            data.lastpos_end = null;
            data.firstpos_end = null;
        }
    }

    @EventHandler
    void event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        for (HunterData  hd : plugin.hunters) {
            if (hd.player == player) {
                plugin.giveTracker(hd, null);
                break;
            }
        }
    }

    @EventHandler
    void event(PlayerDropItemEvent event) {
        if (!plugin.isPlayerRecievingItem) {
            ItemStack item = event.getItemDrop().getItemStack();
            Player player = event.getPlayer();
            for (HunterData hd : plugin.hunters) {
                if (hd.player == player && item.getType() == Material.COMPASS) {
                    hd.cycleTrackedPlayer(plugin);
                    item.setAmount(0);
                    plugin.updateTracker(hd);
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInv = event.getPlayer().getInventory();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK )) {
            if (playerInv.getItemInMainHand().getType() == Material.COMPASS || playerInv.getItemInOffHand().getType() == Material.COMPASS) {
                HunterData hunter = null;
                for (HunterData pd : plugin.hunters) {
                    if (pd.player == player) {
                        hunter = pd;
                        break;
                    }
                }
                if (hunter != null) {
                    plugin.updateTracker(hunter);
                }
            }
        }
    }
}
