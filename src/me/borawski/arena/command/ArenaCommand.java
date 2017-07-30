package me.borawski.arena.command;

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
        if(getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
            player.sendMessage(getRegisteringPlugin().getPrefix() + "§cYou are currently teleporting in/out of the arena, wait for the warmup to finish and try again.");
            return;
        }

        if(getRegisteringPlugin().getArenaManager().isPlaying(player.getUniqueId())) {
            Location location = getRegisteringPlugin().getArenaManager().getPreviousLocations().get(player.getUniqueId());
            getRegisteringPlugin().getWarmupManager().add(player.getUniqueId());
            new BukkitRunnable() {
                public void run() {
                    if(!getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
                        return;
                    }
                    player.teleport(location);
                    player.sendMessage(getRegisteringPlugin().getPrefix() + "You were teleported to your last known location.");
                    getRegisteringPlugin().getWarmupManager().remove(player.getUniqueId());
                }
            }.runTaskLaterAsynchronously(getRegisteringPlugin(), 20*5L);
        } else {
            Location location = player.getLocation();
            getRegisteringPlugin().getArenaManager().getPreviousLocations().put(player.getUniqueId(), location);
            getRegisteringPlugin().getWarmupManager().add(player.getUniqueId());
            new BukkitRunnable() {
                public void run() {
                    if(!getRegisteringPlugin().getWarmupManager().isWarming(player.getUniqueId())) {
                        return;
                    }

                    getRegisteringPlugin().getArenaManager().addPlayer(player.getUniqueId());
                    getRegisteringPlugin().getArenaManager().sendTo(player.getUniqueId());
                    player.sendMessage(getRegisteringPlugin().getPrefix() + "Your previous location has been saved.");
                }
            }.runTaskLaterAsynchronously(getRegisteringPlugin(), 20*5L);
        }

    }
}
