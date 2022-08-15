package com.njbradley.compassplugin;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class PlayerData {
    public Player player;
    public Location firstpos_overworld = null;
    public Location firstpos_nether = null;
    public Location firstpos_end = null;
    public Location lastpos_overworld = null;
    public Location lastpos_nether = null;
    public Location lastpos_end = null;

    public PlayerData(Player newplayer) {
        player = newplayer;
        updateLocation();
    }

    public double getDistanceTo(PlayerData otherplayer) {
        Environment player_env = player.getWorld().getEnvironment();
        Environment env = otherplayer.player.getWorld().getEnvironment();
        Location start = otherplayer.getLastLocation(env);
        double distance = 0;
        while (env != player_env) {
            Location goal = getLastLocation(env);
            if (goal == null || start == null) {
                return -1;
            }
            distance += start.distance(goal);
            if (env == Environment.NETHER || env == Environment.THE_END) {
                env = Environment.NORMAL;
            } else if (env == Environment.NORMAL) {
                env = player_env;
            }
            start = getFirstLocation(env);
        }
        distance += start.distance(getLastLocation(player_env));
        return distance;
    }

    public Location getLastLocation(Environment env) {
        if (env == Environment.NORMAL) {
            return lastpos_overworld;
        } else if (env == Environment.NETHER) {
            return lastpos_nether;
        } else if (env == Environment.THE_END) {
            return lastpos_end;
        }
        return null;
    }

    public Location getFirstLocation(Environment env) {
        if (env == Environment.NORMAL) {
            return firstpos_overworld;
        } else if (env == Environment.NETHER) {
            return firstpos_nether;
        } else if (env == Environment.THE_END) {
            return firstpos_end;
        }
        return null;
    }

    public void updateLocation() {
        Location newlocation = player.getLocation();
        updateLastLocation(newlocation);
    }

    public void updateLastLocation(Location newlocation) {
        if (newlocation.getWorld().getEnvironment() == Environment.NORMAL) {
            lastpos_overworld = newlocation;
        } else if (newlocation.getWorld().getEnvironment() == Environment.NETHER) {
            lastpos_nether = newlocation;
        } else if (newlocation.getWorld().getEnvironment() == Environment.THE_END) {
            lastpos_end = newlocation;
        }
    }

    public void updateFirstLocation(Location newlocation) {
        if (newlocation.getWorld().getEnvironment() == Environment.NORMAL) {
            firstpos_overworld = newlocation;
        } else if (newlocation.getWorld().getEnvironment() == Environment.NETHER) {
            firstpos_nether = newlocation;
        } else if (newlocation.getWorld().getEnvironment() == Environment.THE_END) {
            firstpos_end = newlocation;
        }
    }
}
