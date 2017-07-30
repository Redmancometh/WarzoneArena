package me.borawski.arena.command;

import me.borawski.arena.ArenaPlugin;
import org.bukkit.command.CommandSender;

/**
 * Created by Ethan on 7/30/2017.
 */
public interface Command {

    String getName();

    String getPermission();

    void execute(CommandSender sender, String[] args);

    default ArenaPlugin getRegisteringPlugin() {
        return ArenaPlugin.getInstance();
    }

}
