package me.borawski.arena.arena;

import me.borawski.arena.util.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 7/30/2017.
 */
public interface Arena {

    String getWorld();

    int[] getCorner1();

    int[] getCorner2();

    boolean doesNotify();

    List<UUID> getAttendees();

    default List<Location> getSpawnpoints() {
        return new ArrayList<Location>() {
            {
                add(LocationUtil.getLocation(getWorld(), "-299.5,85.0,-47.5"));
                add(LocationUtil.getLocation(getWorld(), "-317.5,85.0,-47.5"));
                add(LocationUtil.getLocation(getWorld(), "-317.5,85.0,-65.5"));
                add(LocationUtil.getLocation(getWorld(),       "-299.5,85.0,-65.0"));
            }
        };
    }

}
