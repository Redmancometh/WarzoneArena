package me.borawski.arena.listener;

import me.borawski.arena.ArenaPlugin;
import me.borawski.arena.config.ArenaConfig;

import me.borawski.arena.user.ArenaStat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;


/**
 * Created by Ethan on 7/27/2017.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerMoveEvent event) {
        if (!ArenaPlugin.getInstance().getWarmupManager().isWarming(event.getPlayer().getUniqueId())) {
            return;
        }

        int fromX = event.getFrom().getBlockX();
        int fromZ = event.getFrom().getBlockZ();
        int toX = event.getTo().getBlockX();
        int toZ = event.getTo().getBlockZ();

        // Player has moved. //
        if (fromX != toX || fromZ != toZ) {
            if (ArenaPlugin.getInstance().getWarmupManager().isWarming(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(ArenaPlugin.getInstance().getPrefix() + "§cYour warzone arena warmup has been cancelled!");
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
                }
            }
        }
    }


}
