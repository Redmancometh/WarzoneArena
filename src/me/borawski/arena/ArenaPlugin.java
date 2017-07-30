package me.borawski.arena;

import com.redmancometh.redcore.RedPlugin;
import com.redmancometh.redcore.config.ConfigManager;
import com.redmancometh.redcore.mediators.ObjectManager;
import me.borawski.arena.arena.ArenaManager;
import me.borawski.arena.arena.FactionsWarzoneArena;
import me.borawski.arena.config.ArenaConfig;
import me.borawski.arena.listener.PlayerListener;
import me.borawski.arena.user.ArenaPlayer;
import me.borawski.arena.user.StatManager;
import me.borawski.arena.warmup.WarmupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private ConfigManager<ArenaConfig> configManager;
    private List<Class> classList;
    private PlayerManager objectManager;
    private StatManager statManager;

    private FactionsWarzoneArena arena;
    private ArenaManager arenaManager;
    private WarmupManager warmupManager;

    @Override
    public void onEnable() {
        instance = this;
        factory = null;
        configManager = new ConfigManager<ArenaConfig>("config.json", ArenaConfig.class);
        classList = new CopyOnWriteArrayList<Class>();

        getMappedClasses().add(ArenaPlayer.class);
        getMappedClasses().add(UUID.class);

        configManager.init(this);
        this.enable();
        objectManager = new PlayerManager();
        statManager = new StatManager();

        arena = new FactionsWarzoneArena();
        arenaManager = new ArenaManager(arena);
        warmupManager = new WarmupManager();

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

    public FactionsWarzoneArena getArena() {
        return arena;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public WarmupManager getWarmupManager() {
        return warmupManager;
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Arkham" + ChatColor.WHITE + "" + ChatColor.BOLD + "Arena " + ChatColor.RESET + "" + ChatColor.YELLOW + message);
    }

    public String getPrefix() {
        return ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Arkham" + ChatColor.WHITE + "" + ChatColor.BOLD + "Arena " + ChatColor.RESET + "" + ChatColor.GRAY;
    }

    public class PlayerManager extends ObjectManager<ArenaPlayer> {

        public PlayerManager() {
            super(ArenaPlayer.class);
        }

    }

}
