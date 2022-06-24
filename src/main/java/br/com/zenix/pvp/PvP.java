package br.com.zenix.pvp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.Core;
import br.com.zenix.pvp.managers.Manager;

public class PvP extends Core {

	private static Manager manager;

	public void onLoad() {
		super.onLoad();
		
		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setAutoSave(false);
			world.setWeatherDuration(1000);
			world.setTime(6000L);
		}
	}

	public void onEnable() {

		super.onEnable();
		manager = new Manager(this);

		if (!isCorrectlyStarted())
			return;
	}

	public void onDisable() {
		super.onDisable();

		for (Player player : Bukkit.getOnlinePlayers()) {
			getManager().getGamerManager().getGamer(player).update();
		}
	}

	public static Manager getManager() {
		return manager;
	}
}
