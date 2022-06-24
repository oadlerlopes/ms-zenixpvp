package br.com.zenix.pvp.managers.managements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;
import br.com.zenix.pvp.warps.type.WarpType;

public class ConfigManager extends Management {

	public ConfigManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public String getConfig(String obj) {
		return getManager().getPlugin().getConfig().getString("Mysql." + obj);
	}

	public void registerInConfig(Player player, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = player.getLocation();

		file.set(config + ".world", location.getWorld().getName());
		file.set(config + ".x", location.getX());
		file.set(config + ".y", location.getY());
		file.set(config + ".z", location.getZ());
		file.set(config + ".pitch", location.getPitch());
		file.set(config + ".yaw", location.getYaw());

		getManager().getPlugin().saveConfig();
	}

	public void registerInConfig(Object where, Object toSet) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		file.set(String.valueOf(where), String.valueOf(toSet));
		getManager().getPlugin().saveConfig();
	}

	public Location getLocationFromConfig(String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = new Location(Bukkit.getWorld("world"), file.getDouble(config + ".x"),
				file.getDouble(config + ".y"), file.getDouble(config + ".z"));

		location.setPitch(file.getLong(config + ".pitch"));
		location.setYaw(file.getLong(config + ".yaw"));

		return location;
	}

	public Location getLocationFromConfig(String config, String world) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		Location location = new Location(Bukkit.getWorld(world), file.getDouble(config + ".x"),
				file.getDouble(config + ".y"), file.getDouble(config + ".z"));

		location.setPitch(file.getLong(config + ".pitch"));
		location.setYaw(file.getLong(config + ".yaw"));

		return location;
	}

	public void teleportPlayer(Player player, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();

		if (!file.contains(config + ".x"))
			return;

		player.teleport(getLocationFromConfig(config, "world"));
	}

	public void teleportPlayer(Player player, WarpType warp, String config) {
		FileConfiguration file = getManager().getPlugin().getConfig();

		if (!file.contains(config + ".x"))
			return;

		if (warp.equals(WarpType.ONE_VS_ONE)) {
			player.teleport(getLocationFromConfig(config, "1v1"));
			return;
		}

		player.teleport(getLocationFromConfig(config, "world"));
	}

	public void teleportPlayer(Player player, WarpType warp) {
		if (warp.equals(WarpType.ONE_VS_ONE)) {
			player.teleport(new Location(Bukkit.getWorld("1v1"), 104, 7, -55));
		}
		if (warp.equals(WarpType.FPS)) {
			player.teleport(new Location(Bukkit.getWorld("fps"), 0, 71, 0));
		}
		if (warp.equals(WarpType.LAVA)) {
			player.teleport(new Location(Bukkit.getWorld("lava"), 0, 91, 0));
		}
	}

	public int getMaxId(String config, int start) {
		FileConfiguration file = getManager().getPlugin().getConfig();
		int max = 0;

		for (int i = start; i < 100; i++)
			if (file.contains(config + i))
				max = i;

		return max;
	}

}
