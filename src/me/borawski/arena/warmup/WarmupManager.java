package me.borawski.arena.warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 7/30/2017.
 */
public class WarmupManager {

    private List<UUID> warmupCache;

    public WarmupManager() {
        this.warmupCache = new ArrayList<>();
    }

    public List<UUID> getWarmupCache() {
        return warmupCache;
    }

    public void add(UUID uuid) {
        getWarmupCache().add(uuid);
    }

    public void remove(UUID uuid) throws NullPointerException {
        getWarmupCache().remove(uuid);
    }

    public boolean isWarming(UUID uuid) {
        return getWarmupCache().contains(uuid);
    }
}
