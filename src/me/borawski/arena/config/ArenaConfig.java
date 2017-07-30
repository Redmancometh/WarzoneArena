package me.borawski.arena.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 7/27/2017.
 */
public class ArenaConfig {

    public final static List<String> blockedCommands = new ArrayList<String>() {
        {
            add("sethome");
            add("spawn");
            add("tpaccept");
            add("tpa");
            add("home");
            add("gamemode");
        }
    };

}
