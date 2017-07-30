package me.borawski.arena.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Ethan on 7/30/2017.
 */
public class LocationUtil {

    /**
     * Parses a location from config string
     *
     * @param world
     * @param path
     * @return
     */
    public static Location getLocation(String world, String path) {
        String[] strings = path.split(",");
        double x = Double.parseDouble(strings[0]);
        double y = Double.parseDouble(strings[1]);
        double z = Double.parseDouble(strings[2]);

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

}
