package me.borawski.arena.listener;

import me.borawski.arena.ArenaPlugin;
import me.borawski.arena.config.ArenaConfig;

import me.borawski.arena.cosmetic.KillstreakCosmetic;
import me.borawski.arena.user.ArenaPlayer;
import me.borawski.arena.user.ArenaStat;
import me.borawski.arena.util.ChatUtil;
import me.borawski.arena.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ExecutionException;


/**
 * Created by Ethan on 7/27/2017.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerMoveEvent event) {
        int fromX = event.getFrom().getBlockX();
        int fromZ = event.getFrom().getBlockZ();
        int toX = event.getTo().getBlockX();
        int toZ = event.getTo().getBlockZ();

        // Player has moved. //
        if (fromX != toX || fromZ != toZ) {
            if (ArenaPlugin.getInstance().getWarmupManager().isWarming(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(ArenaPlugin.getInstance().getPrefix() + "§cYour warzone arena warmup has been cancelled!");
                ArenaPlugin.getInstance().getWarmupManager().remove(event.getPlayer().getUniqueId());
                return;
            }

            if (ArenaPlugin.getInstance().getArenaManager().isInside(
                    event.getTo(),
                    new Location(Bukkit.getWorld("world"), ArenaPlugin.getInstance().getArenaManager().getArena().getCorner1()[0], 0, ArenaPlugin.getInstance().getArenaManager().getArena().getCorner1()[1]),
                    new Location(Bukkit.getWorld("world"), ArenaPlugin.getInstance().getArenaManager().getArena().getCorner2()[0], 0, ArenaPlugin.getInstance().getArenaManager().getArena().getCorner2()[1]))
                    && !ArenaPlugin.getInstance().getArenaManager().getPlayersInsideArena().contains(event.getPlayer().getUniqueId())) {

                ArenaPlugin.getInstance().getArenaManager().addPlayer(event.getPlayer().getUniqueId());
                ArenaPlugin.getInstance().getArenaManager().getPlayersInsideArena().add(event.getPlayer().getUniqueId());

                Player player = event.getPlayer();
                ArenaPlayer arenaPlayer = null;
                try {
                    arenaPlayer = ArenaPlugin.getInstance().getStatManager().getRecord(player.getUniqueId()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                player.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------------");
                ChatUtil.sendCenteredMessage(player, ArenaPlugin.getInstance().getPrefix().replace(" ", ""));
                player.sendMessage("");
                ChatUtil.sendCenteredMessage(player, ChatColor.GREEN + "Welcome to the arena!");
                ChatUtil.sendCenteredMessage(player, ChatColor.GRAY + "Last arena entry: §e" + TimeUtil.getPrettyTime(arenaPlayer.getStat(ArenaStat.LAST_LOGIN)));
                ChatUtil.sendCenteredMessage(player, ChatColor.GRAY + "Average KDR: §e" + arenaPlayer.getStat(ArenaStat.KILLS)/arenaPlayer.getStat(ArenaStat.DEATHS));
                player.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------------");
            }
        }
    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (!ArenaPlugin.getInstance().getArenaManager().isPlaying(p.getUniqueId())) {
            return;
        }

        ArenaConfig.blockedCommands.forEach((command) -> {
            if (event.getMessage().startsWith("/" + command)) {
                event.setCancelled(true);
                p.sendMessage(ArenaPlugin.getInstance().getPrefix() + "§cYou cannot do that from within the warzone arena!");
            }
        });

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (ArenaPlugin.getInstance().getArenaManager().isPlaying(event.getDamager().getUniqueId())) {
                Player target = (Player) event.getEntity();
                if (target.getHealth() - event.getDamage() < 0.0D) {
                    ArenaPlugin.getInstance().getArenaManager().remove(target.getUniqueId());
                    target.sendMessage(ArenaPlugin.getInstance().getPrefix() + "§cYou were killed! So your last location was deleted");
                    target.sendMessage(ArenaPlugin.getInstance().getPrefix() + "§eUse /arena to try your luck in the warzone arena again!");

                    Player player = (Player) event.getDamager();
                    ArenaPlugin.getInstance().getStatManager().incrementStat(player, ArenaStat.KILLS);
                    ArenaPlugin.getInstance().getStatManager().incrementStat(target, ArenaStat.DEATHS);
                    ArenaPlugin.getInstance().runAsync(ArenaPlugin.getInstance().getArenaManager().computeNewKill(player.getUniqueId()));
                    if(ArenaPlugin.getInstance().getKillstreakCosmeticManager().getKillstreakAnimation().contains(player.getUniqueId())) {
                        ArenaPlugin.getInstance().getKillstreakCosmeticManager().getKillstreakAnimation().remove(player.getUniqueId());
                    }

                    if(ArenaPlugin.getInstance().getKillstreakCosmeticManager().getKillstreakAnimation().contains(target.getUniqueId())) {
                        ArenaPlugin.getInstance().getKillstreakCosmeticManager().getKillstreakAnimation().remove(target.getUniqueId());
                    }

                    new BukkitRunnable() {
                      public void run() {
                          ArenaPlugin.getInstance().runAsync(new KillstreakCosmetic().run(player.getUniqueId()));
                      }
                    }.runTaskLaterAsynchronously(ArenaPlugin.getInstance(), 2L);
                }
            }
        }
    }


}
