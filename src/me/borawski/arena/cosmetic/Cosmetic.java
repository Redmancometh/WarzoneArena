package me.borawski.arena.cosmetic;

import me.borawski.arena.util.Callback;

import java.util.UUID;

/**
 * Created by Ethan on 8/9/2017.
 */
public interface Cosmetic {
    
    String getName();
    
    Callback run(UUID player);
    
}
