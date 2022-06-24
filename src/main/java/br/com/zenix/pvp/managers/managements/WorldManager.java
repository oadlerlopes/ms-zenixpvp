package br.com.zenix.pvp.managers.managements;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;

public class WorldManager extends Management {

	public WorldManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return loadWorlds();
	}

	public boolean loadWorlds() {
		getManager().getPlugin().getServer().createWorld(new WorldCreator("1v1")).setAutoSave(false);
		getLogger().log("The world '1v1' has loaded correcly.");

		getManager().getPlugin().getServer().createWorld(new WorldCreator("lava")).setAutoSave(false);
		getLogger().log("The world 'lava' has loaded correcly.");
		
		getManager().getPlugin().getServer().createWorld(new WorldCreator("fps")).setAutoSave(false);
		getLogger().log("The world 'fps' has loaded correcly.");

		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			if (world.hasStorm()) {
				world.setStorm(false);
			}
			world.setWeatherDuration(1000);
			world.setTime(6000L);
			world.setDifficulty(Difficulty.PEACEFUL);
			world.setWeatherDuration(999999999);
			world.setGameRuleValue("doDaylightCycle", "false");
		}

		return true;
	}
}
