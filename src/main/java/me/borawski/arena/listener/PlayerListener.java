package me.borawski.arena.listener;

import me.borawski.arena.ArenaPlugin;
import me.borawski.arena.user.ArenaStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Ethan on 7/27/2017.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerLoginEvent event) {
        final long time = System.currentTimeMillis();
        ArenaPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(ArenaPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                ArenaPlugin.getInstance().getStatManager().saveAndPurge(event.getPlayer());
                ArenaPlugin.getInstance().getStatManager().setStat(event.getPlayer(), ArenaStat.LAST_LOGIN, time);
                System.out.println("[WarArena] Updated data for: " + event.getPlayer().getName() + " set last login to '" + time + "' (took " + (System.currentTimeMillis() - time) + "ms)");
            }
        });
    }

}
