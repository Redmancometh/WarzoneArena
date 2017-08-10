package me.borawski.arena.cosmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ethan on 8/9/2017.
 */
public class KillstreakCosmeticManager {

    public List<UUID> killstreakAnimation;

    public KillstreakCosmeticManager() {
        killstreakAnimation = new ArrayList<>();
    }

    public List<UUID> getKillstreakAnimation() {
        return killstreakAnimation;
    }

    public void init() {
        KillstreakCosmetic killstreakCosmetic = new KillstreakCosmetic();

    }

}
