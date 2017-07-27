package me.borawski.arena;

import com.redmancometh.redcore.RedPlugin;
import com.redmancometh.redcore.config.ConfigManager;
import com.redmancometh.redcore.mediators.ObjectManager;
import me.borawski.arena.config.ArenaConfig;
import me.borawski.arena.listener.PlayerListener;
import me.borawski.arena.user.ArenaPlayer;
import me.borawski.arena.user.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ethan on 7/27/2017.
 */
public class ArenaPlugin extends JavaPlugin implements RedPlugin {

    private static ArenaPlugin instance;
    private SessionFactory factory;
    private List<Class> classList;
    private PlayerManager objectManager;
    private ConfigManager<ArenaConfig> arenaConfig;
    private StatManager statManager;

    @Override
    public void onEnable() {
        instance = this;
        classList = new CopyOnWriteArrayList<Class>();
        arenaConfig = new ConfigManager<ArenaConfig>("arena_properties.json", ArenaConfig.class);
        objectManager = new PlayerManager();

        classList.add(ArenaPlayer.class);
        classList.add(UUID.class);
        arenaConfig.init(this);

        this.enable();

        statManager = new StatManager();
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), getInstance());
    }

    public static ArenaPlugin getInstance() {
        return instance;
    }

    public List<Class> getMappedClasses() {
        return classList;
    }

    public JavaPlugin getBukkitPlugin() {
        return instance;
    }

    public ObjectManager getManager() {
        return objectManager;
    }

    public SessionFactory getInternalFactory() {
        return factory;
    }

    public void setInternalFactory(SessionFactory sessionFactory) {
        this.factory = sessionFactory;
    }

    public StatManager getStatManager() {
        return statManager;
    }

    public void setStatManager(StatManager statManager) {
        this.statManager = statManager;
    }

    public class PlayerManager extends ObjectManager<ArenaPlayer> {

        public PlayerManager() {
            super(ArenaPlayer.class);
        }

    }

}
