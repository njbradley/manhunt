package com.njbradley.compassplugin;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.GameRule;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import com.njbradley.compassplugin.commands.*;
import com.njbradley.compassplugin.listeners.Listeners;

public class Main extends JavaPlugin {
    World world;
    public List<PlayerData> runners;
    public List<HunterData> hunters;
    public int distanceResolution = 100;
    public RemovePlayer remover;
    public boolean isPlayerRecievingItem = false;

    @Override
    public void onEnable() {
        runners = new ArrayList<PlayerData>();
        hunters = new ArrayList<HunterData>();
        getLogger().info("The manhunt plugin has been enabled.");
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        new AddHunter(this);
        new AddRunner(this);
        remover = new RemovePlayer(this);
        new ListPlayers(this);
        new Settings(this);
    }

    public boolean canTrackHunters() {
        return runners.size() == 0;
    }

    public boolean safeToReplace(ItemStack item) {
        return item == null || item.getType() == Material.COMPASS || item.getType() == Material.AIR;
    }

    public void giveTracker(HunterData player, PlayerData runner) {
        PlayerInventory playerInv = player.player.getInventory();
        if (safeToReplace(playerInv.getItem(8))) {
            giveTrackerSlot(player, runner, "hotbar.8");
        } else {
            giveTrackerSlot(player, runner, "");
        }
    }

      public void giveTrackerMainHand(HunterData player, PlayerData runner) {
        if (safeToReplace(player.player.getInventory().getItemInMainHand())) {
            giveTrackerSlot(player, runner, "weapon.mainhand");
        } else {
            giveTrackerSlot(player, runner, "");
        }
    }

    public void giveTrackerOffHand(HunterData player, PlayerData runner) {
        if (safeToReplace(player.player.getInventory().getItemInOffHand())) {
            giveTrackerSlot(player, runner, "weapon.offhand");
        } else {
            giveTrackerSlot(player, runner, "");
        }
    }

    void giveTrackerSlot(HunterData hunter, PlayerData runner, String slotname) {
        Player player = hunter.player;
        int x = 0;
        int y = 0;
        int z = 0;
        String dimension = "the_end";
        String compassName;
        if (runner != null) {
            Location loc = hunter.trackingLocationTo(runner);
            if (loc.getWorld().getEnvironment() == Environment.NORMAL) {
                dimension = "overworld";
            } else if (loc.getWorld().getEnvironment() == Environment.NETHER) {
                dimension = "the_nether";
            } else if (loc.getWorld().getEnvironment() == Environment.THE_END) {
                dimension = "the_end";
            }
            x = loc.getBlockX();
            y = loc.getBlockY();
            z = loc.getBlockZ();

            compassName = "";
            if (hunter.trackedPlayer == null) {
                compassName += "Closest: " + runner.player.getDisplayName();
            } else {
                compassName += "Locked: " + hunter.trackedPlayer.player.getDisplayName();
            }
            if (distanceResolution > 0) {
                double dist = hunter.getDistanceTo(runner);
                compassName += " (" + (int)(Math.round(dist / distanceResolution) * distanceResolution) + " blocks)";
            }
        } else {
            compassName = "No players";
        }
        if (hunter.trackedPlayerToggle) {
            compassName = " " + compassName + " ";
        }
        hunter.trackedPlayerToggle = !hunter.trackedPlayerToggle;

        //String nbt = "{\"LodestonePos\":{\"X\":" + x + ",\"Y\":" + y + ",\"Z\":" + z + "},\"LodestoneDimension\":\"" + dimension
            //+ "\",\"LodestoneTracked\":" + (runner == null ? 1 : 0) + "b, \"display\": {\"Name\":'{\"text\":\"" + compassName + "\"}'}}";
        String nbt = "[minecraft:lodestone_tracker={target:{pos:[" + x + "," + y + "," + z + "],dimension:'" + dimension + "'},tracked:false}]";

        String command;
        getServer().getWorlds().get(0).setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
        if (slotname.equals("")) {
            isPlayerRecievingItem = true;
            getServer().dispatchCommand(player, "give @s compass" + nbt);
            isPlayerRecievingItem = false;
        } else {
            getServer().dispatchCommand(player, "item replace entity @s " + slotname + " with compass" + nbt);
        }
        getServer().getWorlds().get(0).setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);
        player.updateInventory();
    }

    public void clearTracker(Player player) {
        player.getInventory().remove(Material.COMPASS);
    }

    public void updateTracker(HunterData hunter) {
        System.out.println("updating tracker " + hunter.player.getDisplayName());
        double closest = 0;
        PlayerData runner = null;
        hunter.updateLocation();
        PlayerInventory playerInv = hunter.player.getInventory();

        for (PlayerData rd : hunter.getTrackedRunners(this)) {
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

        Location tracking_loc = null;
        if (runner != null) {
            tracking_loc = hunter.trackingLocationTo(runner);
        }
        System.out.println(" already item " + playerInv.getItemInMainHand());
        if (playerInv.getItemInOffHand().getType() == Material.COMPASS) {
            System.out.println(" giving " + hunter.player.getDisplayName() + " off hand");
            giveTrackerOffHand(hunter, runner);
        } else {
            System.out.println(" giving " + hunter.player.getDisplayName() + " main hand");
            giveTrackerMainHand(hunter, runner);
        }
    }

    @Override
    public void onDisable() {
        List<Player> players = new ArrayList<Player>();
        for (PlayerData data : runners) {
            players.add(data.player);
        }
        for (HunterData data : hunters) {
            players.add(data.player);
        }
        for (Player player : players) {
            remover.remove(player);
        }
        getLogger().info("The manhunt plugin has been disabled.");
    }
}
