package me.borawski.arena.arena;

import me.borawski.arena.ArenaPlugin;
import me.borawski.arena.user.ArenaPlayer;
import me.borawski.arena.user.ArenaStat;
import me.borawski.arena.util.Callback;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ethan on 7/30/2017.
 */
public class ArenaManager {

    private Arena arena;
    private Map<UUID, Integer> killstreak;
    private Map<UUID, Location> previousLocations;
    private Random randomSpawn;

    public ArenaManager(Arena arena) {
        this.arena = arena;
        this.randomSpawn = new Random();
        this.killstreak = new ConcurrentHashMap<>();
        this.previousLocations = new ConcurrentHashMap<>();
    }

    public Arena getArena() {
        return arena;
    }

    public Map<UUID, Integer> getKillstreak() {
        return killstreak;
    }

    public Integer getCurrentKillstreak(UUID uuid) {
        return getKillstreak().get(uuid);
    }

    public Map<UUID, Location> getPreviousLocations() {
        return previousLocations;
    }

    public Callback computeNewKill(UUID uuid) {
        return () -> {
            try {
                ArenaPlayer player = ArenaPlugin.getInstance().getStatManager().getRecord(uuid).get();
                ArenaPlugin.getInstance().getStatManager().incrementStat(Bukkit.getPlayer(uuid), ArenaStat.KILLS);
                if(getCurrentKillstreak(uuid) >= player.getStat(ArenaStat.HIGHEST)) {
                    ArenaPlugin.getInstance().getStatManager().setStat(Bukkit.getPlayer(uuid), ArenaStat.HIGHEST, getCurrentKillstreak(uuid));
                    if(getCurrentKillstreak(uuid) % 5 == 0 && getArena().doesNotify()) {
                        ArenaPlugin.getInstance().broadcast(ChatColor.GREEN + "" + Bukkit.getPlayer(uuid).getName() + "" + ChatColor.YELLOW + " has reached a §c§l§nnew§r§e killstreak of §c§l§n" + getCurrentKillstreak(uuid));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isPlaying(UUID uuid) {
        return getArena().getAttendees().contains(uuid);
    }

    public void remove(UUID uuid) {
        getArena().getAttendees().remove(uuid);
        getKillstreak().remove(uuid);
        getPreviousLocations().remove(uuid);
    }

    public boolean addPlayer(UUID uuid) {
        getArena().getAttendees().add(uuid);
        getKillstreak().put(uuid, 0);
        getPreviousLocations().put(uuid, Bukkit.getPlayer(uuid).getLocation());
        return true;
    }

    public void sendTo(UUID uuid) {
        // Randomize spawnpoint
        int index = randomSpawn.nextInt(getArena().getSpawnpoints().size());
        Location location = getArena().getSpawnpoints().get(index);
        Bukkit.getPlayer(uuid).teleport(location);
    }

}
