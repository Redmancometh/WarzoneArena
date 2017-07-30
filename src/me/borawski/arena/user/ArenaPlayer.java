package me.borawski.arena.user;

import com.redmancometh.redcore.Defaultable;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 7/27/2017.
 */
@Entity(name = "arena_player")
public class ArenaPlayer implements Serializable, Defaultable<UUID> {

    @Column(name = "owner", unique = true)
    @Id
    @Type(type = "uuid-char")
    private UUID owner;

    @MapKeyClass(value = ArenaStat.class)
    @ElementCollection(targetClass =  Long.class)
    private Map<ArenaStat, Long> playerStats = new HashMap();

    public ArenaPlayer() {

    }

    public UUID getUniqueId() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Long setStat(ArenaStat type, long i) {
        return playerStats.put(type, i);
    }

    public Long getStat(ArenaStat type) {
        return playerStats.get(type);
    }

    @Override
    public void setDefaults(UUID key) {
        this.setOwner(key);
        final Map<ArenaStat, Long> playerStats = new ConcurrentHashMap<ArenaStat, Long>();
        for (ArenaStat arenaStat : ArenaStat.values()) {
            playerStats.put(arenaStat, 1L);
        }
        this.playerStats = playerStats;
    }

    public Map<ArenaStat, Long> getStatMap() {
        return playerStats;
    }

    public void setStatMap(Map<ArenaStat, Long> playerStats) {
        this.playerStats = playerStats;
    }
}
