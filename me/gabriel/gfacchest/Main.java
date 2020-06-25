package me.gabriel.gfacchest;

import java.io.File;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.gabriel.gfacchest.eventos.EventFactionsDisband;
import me.gabriel.gfacchest.eventos.PlayerCommandPreprocessEvent;
import me.gabriel.gfacchest.manager.inventory.Manager_Inventory;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	private static Main main;
    private Manager_Inventory chestManager;
    private File chestFolder;
    public static Economy eco;
    private static PlayerPoints playerPoints;

	@Override
	public void onEnable() {
		enablePlugin();
		gerarConfigs();
		carregarConfigs();
		registrarEventos();
		registrarComandos();
	}

	@Override
	public void onDisable() {
		disablePlugin();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enablePlugin() {
		main = this;
		saveDefaultConfig();
        playerPoints = (PlayerPoints)PlayerPoints.getPlugin((Class)PlayerPoints.class);
        Main.hookEconomy();
        this.chestFolder = new File(this.getDataFolder(), "chests");
        this.chestManager = new Manager_Inventory(chestFolder);
        final int autosaveInterval = Main.get().getConfig().getInt("Config.Auto-Save") * 1200;
        if (autosaveInterval > 0) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
                public void run() {
                    final int savedChests = Main.this.chestManager.save();
                    if (savedChests > 0) {
                    	Bukkit.getConsoleSender().sendMessage("§a[gFacChest] Auto-Save > Foi salvo um total de " + savedChests + " baú(s) de facções.");
                    }
                }
            }, (long)autosaveInterval, (long)autosaveInterval);
        }
	}

	private void gerarConfigs() {
	}

	private void carregarConfigs() {
	}

	private void registrarComandos() {
	}

	private void registrarEventos() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new EventFactionsDisband(chestManager), this);
		pm.registerEvents(new PlayerCommandPreprocessEvent(chestManager), this);
	}

	private void disablePlugin() {
		try {
	        this.chestManager.save();
			HandlerList.unregisterAll(this);
			Bukkit.getScheduler().cancelTasks(this);
		} catch (Throwable e) {}
	}
	
	public static Main get() {
		return main;
	}
	
	public static PlayerPoints getCash() {
		return playerPoints;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void hookEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>)Bukkit.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            Bukkit.getConsoleSender().sendMessage("§a[gFacChest] Vault encontrado. Hooked (Economy)");
            Main.eco = (Economy)economyProvider.getProvider();
        }
    }
}
