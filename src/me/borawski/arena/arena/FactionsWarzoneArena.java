package me.borawski.arena.arena;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 7/30/2017.
 */
public class FactionsWarzoneArena implements Arena {

    public FactionsWarzoneArena() {
    }

    @Override
    public boolean doesNotify() {
        return true;
    }

    @Override
    public List<UUID> getAttendees() {
        return new ArrayList<>();
    }

    @Override
    public String getWorld() {
        return "world";
    }

    @Override
    public int[] getCorner1() {
        return new int[]{-319,-45};
    }

    @Override
    public int[] getCorner2() {
        return new int[]{-297,-67};
    }

}
