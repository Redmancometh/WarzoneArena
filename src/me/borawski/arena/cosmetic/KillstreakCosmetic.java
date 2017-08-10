package me.borawski.arena.cosmetic;

import me.borawski.arena.ArenaPlugin;
import me.borawski.arena.util.Callback;
import me.borawski.arena.util.CustomIS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 8/9/2017.
 */
public class KillstreakCosmetic implements Cosmetic {

    public KillstreakCosmetic() {
    }

    @Override
    public String getName() {
        return "killstreak";
    }

    @Override
    public Callback run(UUID player) {
        return () -> {
            int killstreak = ArenaPlugin.getInstance().getArenaManager().getCurrentKillstreak(player);
            Player user = Bukkit.getPlayer(player);

            if (killstreak >= 5) {
                killstreak = 5;
            }

            List<Entity> entityUUID = new ArrayList<Entity>();

            for (int i = 0; i < killstreak; i++) {
                ArmorStand stand = (ArmorStand) user.getWorld().spawnEntity(user.getLocation().clone().add(0, 1.0, 0), EntityType.ARMOR_STAND);
                stand.setHelmet(new CustomIS().setMaterial(Material.SKULL).get());
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                entityUUID.add(stand);
            }

            new BukkitRunnable() {
                public void run() {
                    if(!ArenaPlugin.getInstance().getKillstreakCosmeticManager().getKillstreakAnimation().contains(user.getUniqueId())) {
                        cancel();
                        return;
                    }

                    entityUUID.forEach((entity) -> {
                        double i = (user.getTicksLived());
                        double degreeRotation = (-i / Math.PI + (i * 5));
                        Vector direction = user.getLocation().clone().toVector().normalize();
                        double x, y, z;

                        x = (direction.getX() * Math.cos(degreeRotation / Math.PI + 10)) + (direction.getZ() * Math.sin(degreeRotation / Math.PI + 10));
                        y = 0;
                        z = (-direction.getX() * Math.sin(degreeRotation / Math.PI + 10) + direction.getZ() + Math.cos(degreeRotation / Math.PI + 10));

                        Vector v = new Vector(x,y,z);
                        entity.teleport(user.getLocation().clone().add(v).add(0,2,0));
                    });
                }
            }.runTaskTimerAsynchronously(ArenaPlugin.getInstance(), 0L, 1L);
        };
    }

}
