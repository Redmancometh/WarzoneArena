package me.borawski.arena.user;

import com.redmancometh.redcore.RedCore;
import com.redmancometh.redcore.databasing.SubDatabase;
import com.redmancometh.redcore.exceptions.ObjectNotPresentException;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Created by Ethan on 7/27/2017.
 */
public class StatManager {

    private SubDatabase<UUID, ArenaPlayer> getSubDB() {
        return RedCore.getInstance().getMasterDB().getSubDBForType(ArenaPlayer.class);
    }

    public CompletableFuture<ArenaPlayer> getRecord(UUID uuid) {
        return getSubDB().getObject(uuid);
    }

    public void setStat(Player p, final ArenaStat stat, final long value) {
        UUID uuid = p.getUniqueId();
        getSubDB().getObject(uuid).thenAccept(new Consumer<ArenaPlayer>() {
            @Override
            public void accept(ArenaPlayer record) {
                record.setStat(stat, value);
            }
        });
    }

    public void incrementStat(Player p, ArenaStat type) {
        incrementStat(p, type, 1);
    }

    public void subtractFromStat(Player p, final ArenaStat type, final long amount) {
        UUID uuid = p.getUniqueId();
        getSubDB().getObject(uuid).thenAccept(new Consumer<ArenaPlayer>() {
            @Override
            public void accept(ArenaPlayer record) {
                record.setStat(type, record.getStat(type) - 1);
            }
        });
    }

    public CompletableFuture<Void> save(Player p) {
        return save(p.getUniqueId());
    }

    public CompletableFuture<Void> save(UUID uuid) {
        return getSubDB().getObject(uuid).thenAccept(new Consumer<ArenaPlayer>() {
            @Override
            public void accept(ArenaPlayer record) {
                StatManager.this.getSubDB().saveObject(record);
            }
        });
    }

    public void saveAndPurge(Player p) {
        final UUID uuid = p.getUniqueId();
        getSubDB().getObject(uuid).thenAccept(new Consumer<ArenaPlayer>() {
            @Override
            public void accept(ArenaPlayer record) {
                try {
                    StatManager.this.getSubDB().saveAndPurge(record, uuid);
                } catch (ObjectNotPresentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void incrementStat(Player p, final ArenaStat type, final int increment) {
        getRecord(p.getUniqueId()).thenAccept(new Consumer<ArenaPlayer>() {
            @Override
            public void accept(ArenaPlayer record) {
                record.getStatMap().compute(type, new BiFunction<ArenaStat, Long, Long>() {
                    @Override
                    public Long apply(ArenaStat t, Long amount) {
                        return amount + increment;
                    }
                });
            }
        });
    }


}
