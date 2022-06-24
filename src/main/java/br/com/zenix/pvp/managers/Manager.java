package br.com.zenix.pvp.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import br.com.zenix.core.plugin.logger.Logger;
import br.com.zenix.core.spigot.Core;
import br.com.zenix.core.spigot.manager.CoreManager;
import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.battle.BattleManager;
import br.com.zenix.pvp.gamer.GamerManager;
import br.com.zenix.pvp.kits.KitManager;
import br.com.zenix.pvp.managers.managements.ClassManager;
import br.com.zenix.pvp.managers.managements.ConfigManager;
import br.com.zenix.pvp.managers.managements.WorldManager;
import br.com.zenix.pvp.player.admin.AdminManager;
import br.com.zenix.pvp.player.admin.Vanish;
import br.com.zenix.pvp.player.scoreboard.PlayerScoreboard;
import br.com.zenix.pvp.utilitaries.Utilitaries;
import br.com.zenix.pvp.utilitaries.Variables;
import br.com.zenix.pvp.warps.WarpManager;

public class Manager {

	private final PvP plugin;
	private final CoreManager coreManager;

	public Utilitaries utils = new Utilitaries();

	private ClassManager classManager;
	private PlayerScoreboard playerScoreboard;
	private BattleManager battleManager;
	private GamerManager gamerManager;
	private AdminManager adminManager;
	private WarpManager warpManager;
	private ConfigManager configManager;
	private WorldManager worldManager;
	private KitManager kitManager;
	
	private Vanish vanish; 

	public Manager(Core core) {
		this.plugin = PvP.getPlugin(PvP.class);
		getPlugin().saveDefaultConfig();

		getLogger().log(
				"Starting the plugin " + plugin.getName() + " version " + plugin.getDescription().getVersion() + "...");

		coreManager = Core.getCoreManager();
		getLogger().log("Making connection with plugin " + coreManager.getPlugin().getName() + " version "
				+ coreManager.getPlugin().getDescription().getVersion() + ".");

		getLogger().log("The plugin " + plugin.getName() + " version " + plugin.getDescription().getVersion()
				+ " was started correcly.");

		String server = Core.getCoreManager().getServerName();
		Integer i = Integer.valueOf(server.charAt(server.length() - 1));

		if (i == 1)
			Variables.FULL_IRON = true;
		else
			Variables.FULL_IRON = false;

		if (getCoreManager().getServerName().toLowerCase().equals("pvp-1")) {
			Variables.FULL_IRON = true;
		}
		
		if (getCoreManager().getServerName().toLowerCase().equals("pvp-3")) {
			Variables.FULL_IRON = true;
		}

		gamerManager = new GamerManager(this);
		if (!gamerManager.correctlyStart()) {
			return;
		}

		playerScoreboard = new PlayerScoreboard(this);
		if (!playerScoreboard.correctlyStart()) {
			return;
		}

		classManager = new ClassManager(this);
		if (!classManager.correctlyStart()) {
			return;
		}

		adminManager = new AdminManager(this);
		if (!adminManager.correctlyStart()) {
			return;
		}

		worldManager = new WorldManager(this);
		if (!worldManager.correctlyStart()) {
			return;
		}

		configManager = new ConfigManager(this);
		if (!configManager.correctlyStart()) {
			return;
		}

		battleManager = new BattleManager(this);
		if (!battleManager.correctlyStart()) {
			return;
		}

		warpManager = new WarpManager(this);
		if (!warpManager.correctlyStart()) {
			return;
		}
		
		vanish = new Vanish(this);
		
		kitManager = new KitManager(this);
		if (!kitManager.correctlyStart()) {
			return;
		}
	}

	public void registerListener(Listener listener) {
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(listener, getPlugin());
	}
	
	public Vanish getVanish() {
		return vanish;
	}

	public PvP getPlugin() {
		return plugin;
	}

	public CoreManager getCoreManager() {
		return coreManager;
	}

	public PlayerScoreboard getPlayerScoreboard() {
		return playerScoreboard;
	}

	public ClassManager getClassManager() {
		return classManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public KitManager getKitManager() {
		return kitManager;
	}

	public BattleManager getBattleManager() {
		return battleManager;
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public BattleManager getOneVsOneManager() {
		return battleManager;
	}

	public WarpManager getWarpTeleport() {
		return warpManager;
	}

	public AdminManager getAdminManager() {
		return adminManager;
	}

	public int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}

	public Logger getLogger() {
		return getPlugin().getLoggerSecondary();
	}

	public GamerManager getGamerManager() {
		return gamerManager;
	}

	public Utilitaries getUtils() {
		return utils;
	}

}
