package me.borawski.arena.command;

import me.borawski.arena.util.LeashUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Ethan on 7/30/2017.
 */
public class ArenaCommand implements Command {

    @Override
    public String getName() {
        return "arena";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("/arena is only for players");
            return;
        }

        Player player = (Player) sender;
        if (getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
            player.sendMessage(getRegisteringPlugin().getPrefix() + "§cYou are currently teleporting in/out of the arena, wait for the warmup to finish and try again.");
            return;
        }

        System.out.println("Player is playing: " + getRegisteringPlugin().getArenaManager().isPlaying(player.getUniqueId()));

        if (getRegisteringPlugin().getArenaManager().isPlaying(player.getUniqueId())) {
            Location location = getRegisteringPlugin().getArenaManager().getPreviousLocations().get(player.getUniqueId());
            getRegisteringPlugin().getWarmupManager().add(player.getUniqueId());
            player.sendMessage(getRegisteringPlugin().getPrefix() + "Teleporting you out of the arena in 5 seconds...");
            new BukkitRunnable() {
                public void run() {
                    if (!getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
                        return;
                    }
                    player.teleport(location);
                    player.sendMessage(getRegisteringPlugin().getPrefix() + "You were teleported to your last known location.");
                    getRegisteringPlugin().getWarmupManager().remove(player.getUniqueId());
                    getRegisteringPlugin().getArenaManager().remove(player.getUniqueId());
                }
            }.runTaskLaterAsynchronously(getRegisteringPlugin(), 20 * 5L);
        } else {
            Location location = player.getLocation().clone();
            getRegisteringPlugin().getArenaManager().getPreviousLocations().put(player.getUniqueId(), location);
            getRegisteringPlugin().getWarmupManager().add(player.getUniqueId());
            player.sendMessage(getRegisteringPlugin().getPrefix() + "Your previous location has been saved.");
            player.sendMessage(getRegisteringPlugin().getPrefix() + "Teleporting you into the arena in 5 seconds...");

            new BukkitRunnable() {
                public void run() {
                    if (!getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
                        return;
                    }

                    getRegisteringPlugin().getWarmupManager().remove(player.getUniqueId());
                    getRegisteringPlugin().getArenaManager().addPlayer(player.getUniqueId());
                    getRegisteringPlugin().getArenaManager().sendTo(player.getUniqueId());
                    player.sendMessage(getRegisteringPlugin().getPrefix() + "You have been teleported to the arena.");
                }
            }.runTaskLater(getRegisteringPlugin(), 20 * 5L);
        }
    }
}
