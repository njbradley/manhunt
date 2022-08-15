package com.njbradley.compassplugin;

import java.util.*;

import org.bukkit.entity.Player;
import org.bukkit.Location;

public class HunterData extends PlayerData {
    public PlayerData trackedPlayer = null;
    public boolean trackedPlayerToggle = false;

    public HunterData(Player player) {
        super(player);
    }

    public List<PlayerData> getTrackedRunners(Main plugin) {
        if (trackedPlayer != null) {
            List<PlayerData> result = new ArrayList<PlayerData>();
            result.add(trackedPlayer);
            return result;
        } else {
            return allRunners(plugin);
        }
    }

    public List<PlayerData> allRunners(Main plugin) {
        List<PlayerData> runners = new ArrayList<PlayerData>();
        if (plugin.canTrackHunters()) {
            for (PlayerData data : plugin.hunters) {
                if (data != this) {
                    runners.add(data);
                }
            }
        }
        for (PlayerData data : plugin.runners) {
            runners.add(data);
        }
        return runners;
    }

    public void cycleTrackedPlayer(Main plugin) {
        for (PlayerData data : allRunners(plugin)) {
            if (trackedPlayer == null) {
                trackedPlayer = data;
                return;
            }
            if (trackedPlayer == data) {
                trackedPlayer = null;
            }
        }
    }

    public Location trackingLocationTo(PlayerData runner) {
        Location tracking_loc = runner.getLastLocation(player.getWorld().getEnvironment());
        if (tracking_loc == null) {
            tracking_loc = getFirstLocation(player.getWorld().getEnvironment());
        }
        return tracking_loc;
    }
}
