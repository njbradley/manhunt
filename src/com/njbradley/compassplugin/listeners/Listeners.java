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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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
    for (PlayerData hd : plugin.hunters) {
      if (hd.player == player) {
        player.getInventory().setItem(8, plugin.createTrackerCompass(null));
        break;
      }
    }
  }
  
  @EventHandler
  void event(PlayerDropItemEvent event) {
    ItemStack item = event.getItemDrop().getItemStack();
    Player player = event.getPlayer();
    for (PlayerData hd : plugin.hunters) {
      if (hd.player == player && item.getType() == Material.COMPASS) {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void onRightClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    PlayerInventory playerInv = event.getPlayer().getInventory();
    if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK )) {
      if (playerInv.getItemInMainHand().getType() == Material.COMPASS || playerInv.getItemInOffHand().getType() == Material.COMPASS) {
        PlayerData hunter = null;
        for (PlayerData pd : plugin.hunters) {
          if (pd.player == player) {
            hunter = pd;
            break;
          }
        }
        if (hunter != null) {
          double closest = 0;
          PlayerData runner = null;
          hunter.updateLocation();
          for (PlayerData rd : plugin.runners) {
            rd.updateLocation();
            double dist = hunter.getDistanceTo(rd);
            if (dist != -1 && (runner == null || dist < closest)) {
              closest = dist;
              runner = rd;
            }
            dist = rd.getDistanceTo(hunter);
            if (dist != -1 && (runner == null || dist < closest)) {
              closest = dist;
              runner = rd;
            }
          }
          if (runner != null) {
            Location tracking_loc = runner.getLastLocation(hunter.player.getWorld().getEnvironment());
            if (tracking_loc == null) {
              tracking_loc = hunter.getFirstLocation(hunter.player.getWorld().getEnvironment());
            }
            if (tracking_loc != null) {
              if (playerInv.getItemInMainHand().getType() == Material.COMPASS) {
                playerInv.setItemInMainHand(plugin.createTrackerCompass(tracking_loc));
              } else {
                playerInv.setItemInOffHand(plugin.createTrackerCompass(tracking_loc));
              }
              player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Currently tracking " + runner.player.getDisplayName() + "."));
            }
          } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("No runners to track."));
          }
        }
      }
    }
  }
}
