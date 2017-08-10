package me.borawski.arena.util;

import me.borawski.arena.ArenaPlugin;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Ethan on 7/31/2017.
 */
public class LeashUtil {

    public static void deployParachute(Player player) {
        Chicken chicken = player.getWorld().spawn(player.getLocation().add(0, 2, 0), Chicken.class);
        chicken.setPassenger(player);

        new BukkitRunnable() {
            public void run() {
                chicken.remove();
            }
        }.runTaskLater(ArenaPlugin.getInstance(), 20L * 5);
    }

}
