package br.com.zenix.pvp.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.data.handler.DataHandler;
import br.com.zenix.core.plugin.data.handler.type.DataType;
import br.com.zenix.core.spigot.player.league.player.PlayerLeague;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.utilitaries.Variables;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.warps.constructor.ItemConstructor;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class GamerDeathEvent extends PvPListener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");

		Player dead = event.getEntity();
		dead.setHealth(20.0D);
		event.getDrops().clear();
		Gamer deadGamer = getManager().getGamerManager().getGamer(dead.getUniqueId());
		deadGamer.setCombat(0);

		if (deadGamer.getWarp().equals(WarpType.ONE_VS_ONE)) {
			getManager().getConfigManager().teleportPlayer(dead, WarpType.ONE_VS_ONE);
			return;
		}

		if (event.getEntity().getKiller() instanceof Player) {

			Player killer = dead.getKiller();

			if (killer != event.getEntity()) {

				Gamer killerGamer = getManager().getGamerManager().getGamer(killer.getUniqueId());

				if (killerGamer.getWarp().equals(WarpType.LAVA))
					return;

				DataHandler dataHandler = killerGamer.getAccount().getDataHandler();

				dataHandler.getValue(DataType.PVP_KILL)
						.setValue(dataHandler.getValue(DataType.PVP_KILL).getValue() + 1);
				dataHandler.getValue(DataType.PVP_KILLSTREAK)
						.setValue(dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() + 1);

				dataHandler.update(DataType.PVP_KILL);
				dataHandler.update(DataType.PVP_KILLSTREAK);

				killer.sendMessage("§e§lKILL §fVocê matou o player §e" + dead.getName() + "§f!");
				new PlayerLeague(killer, dead).prizeLeague();

				if (dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() % 10 == 0) {
					if (dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + killerGamer.getAccount().getPlayer().getName()
								+ "§f conseguiu um §6§lKILLSTREAK DE "
								+ dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() + "§f");
					}
				}

				if (dataHandler.getValue(DataType.PVP_MOST_KILLSTREAK).getValue() < dataHandler
						.getValue(DataType.PVP_KILLSTREAK).getValue()) {
					dataHandler.getValue(DataType.PVP_MOST_KILLSTREAK)
							.setValue(dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue());

					dataHandler.update(DataType.PVP_MOST_KILLSTREAK);
				}

				if (Variables.FULL_IRON
						&& (killerGamer.getWarp().equals(WarpType.FPS) || killerGamer.getWarp().equals(WarpType.NONE))) {
					if (killer.getInventory().getHelmet() != null)
						killer.getInventory().getHelmet().setDurability((short) 0);
					if (killer.getInventory().getChestplate() != null)
						killer.getInventory().getChestplate().setDurability((short) 0);
					if (killer.getInventory().getLeggings() != null)
						killer.getInventory().getLeggings().setDurability((short) 0);
					if (killer.getInventory().getBoots() != null)
						killer.getInventory().getBoots().setDurability((short) 0);
				}

				killerGamer.update();

				if (deadGamer.getAccount() != null) {
					if (deadGamer.getAccount().getDataHandler().getValue(DataType.PVP_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + deadGamer.getAccount().getPlayer().getName()
								+ "§f perdeu seu §6§lKILLSTREAK DE "
								+ deadGamer.getAccount().getDataHandler().getValue(DataType.PVP_KILLSTREAK).getValue()
								+ "§f para §c§l" + killerGamer.getAccount().getPlayer().getName() + "§f");
					}
				}
			}

			DataHandler dataHandler = deadGamer.getAccount().getDataHandler();

			dataHandler.getValue(DataType.PVP_KILLSTREAK).setValue(0);
			dataHandler.getValue(DataType.PVP_DEATH).setValue(dataHandler.getValue(DataType.PVP_DEATH).getValue() + 1);

			deadGamer.getAccount().getPlayer()
					.sendMessage("§4§lDEATH §fVocê morreu para §4§l" + killer.getName() + "§f!");

			dataHandler.update(DataType.PVP_KILLSTREAK);
			dataHandler.update(DataType.PVP_DEATH);

			getManager().getLogger().log("O player " + dead.getName() + " morreu para " + killer.getName() + "!");
		} else {
			if (deadGamer.getWarp() != WarpType.LAVA) {

				DataHandler dataHandler = deadGamer.getAccount().getDataHandler();

				dataHandler.getValue(DataType.PVP_KILLSTREAK).setValue(0);
				dataHandler.getValue(DataType.PVP_DEATH)
						.setValue(dataHandler.getValue(DataType.PVP_DEATH).getValue() + 1);

				dataHandler.update(DataType.PVP_KILLSTREAK);
				dataHandler.update(DataType.PVP_DEATH);
				deadGamer.getAccount().getPlayer().sendMessage("§4§lDEATH §fVocê morreu!");
				

				getManager().getLogger().log("O player " + dead.getName() + " morreu sozinho!");
			}
		}

		DataHandler dataHandler = deadGamer.getAccount().getDataHandler();

		dataHandler.getValue(DataType.PVP_KILLSTREAK).setValue(0);
		dataHandler.update(DataType.PVP_KILLSTREAK);

		for (ItemStack drops : event.getDrops()) {
			Material m = drops.getType();
			if ((m.equals(Material.WOOD_SWORD)) || (m.equals(Material.FLINT)) || (m.equals(Material.WATCH))
					|| (m.equals(Material.BLAZE_ROD)) || (m.equals(Material.STONE_PICKAXE))
					|| (m.equals(Material.WOOD_AXE)) || (m.equals(Material.FIREWORK)) || (m.equals(Material.LEASH))
					|| (m.equals(Material.IRON_FENCE)) || (m.equals(Material.SNOW_BALL))
					|| (m.equals(Material.SLIME_BALL)) || (m.equals(Material.STONE_AXE))
					|| (m.equals(Material.WOOD_HOE)) || (m.equals(Material.STICK)) || (m.equals(Material.FISHING_ROD))
					|| (m.equals(Material.ENDER_PEARL)) || (m.equals(Material.CLAY_BALL)) || (m.equals(Material.PORTAL))
					|| (m.equals(Material.LEATHER_BOOTS)) || (m.equals(Material.NETHER_STAR))
					|| (m.equals(Material.STONE_PLATE)) || (m.equals(Material.GRAVEL))) {
				drops.setAmount(1);
				drops.setType(Material.AIR);
			}
		}

		if (deadGamer.getWarp().equals(WarpType.SPAWN) || deadGamer.getWarp().equals(WarpType.NONE)) {
			dead.teleport(Bukkit.getWorld("world").getSpawnLocation());
			dead.getInventory().clear();
			dead.getInventory().setArmorContents(null);
			getManager().getGamerManager().removePottionEffects(dead);
			getManager().getGamerManager().resetPlayer(dead);

			new BukkitRunnable() {
				public void run() {
					CacheItems.JOIN.build(dead);
					dead.updateInventory();
				}
			}.runTaskLater(getManager().getPlugin(), 2L);

			deadGamer.setPvP(false);
		}

		if (deadGamer.getWarp().equals(WarpType.FPS)) {
			deadGamer.setWarp(WarpType.FPS);
			deadGamer.setPvP(true);
			new ItemConstructor(getManager(), dead, WarpType.FPS);
			getManager().getConfigManager().teleportPlayer(dead, WarpType.FPS);
			new BukkitRunnable() {
				public void run() {
					new ItemConstructor(getManager(), dead, WarpType.FPS);
				}
			}.runTaskLater(getManager().getPlugin(), 2L);
			return;
		}

		if (deadGamer.getWarp().equals(WarpType.LAVA)) {
			deadGamer.setWarp(WarpType.LAVA);
			deadGamer.setPvP(false);
			new ItemConstructor(getManager(), dead, WarpType.LAVA);
			getManager().getConfigManager().teleportPlayer(dead, WarpType.LAVA);
			new BukkitRunnable() {
				public void run() {
					new ItemConstructor(getManager(), dead, WarpType.LAVA);
				}
			}.runTaskLater(getManager().getPlugin(), 2L);
			return;
		}

		deadGamer.removeWaitTime();
		deadGamer.setKit(KitType.NONE);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		Gamer gamer = getManager().getGamerManager().getGamer(event.getPlayer());

		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE)) {
			getManager().getConfigManager().teleportPlayer(player, WarpType.ONE_VS_ONE);
			return;
		}

		if (gamer.getWarp().equals(WarpType.SPAWN) || gamer.getWarp().equals(WarpType.NONE)) {
			event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
		}

		if (gamer.getWarp().equals(WarpType.FPS)) {
			gamer.setWarp(WarpType.FPS);
			gamer.setPvP(true);
			new ItemConstructor(getManager(), player, WarpType.FPS);
			event.setRespawnLocation(new Location(Bukkit.getWorld("world"), 3050, 93, 3045));
			new BukkitRunnable() {
				public void run() {
					new ItemConstructor(getManager(), player, WarpType.FPS);
				}
			}.runTaskLater(getManager().getPlugin(), 2L);
			return;
		}

		if (gamer.getWarp().equals(WarpType.LAVA)) {
			gamer.setWarp(WarpType.LAVA);
			gamer.setPvP(false);
			new ItemConstructor(getManager(), player, WarpType.LAVA);
			event.setRespawnLocation(new Location(Bukkit.getWorld("lava"), 0, 91, 0));
			new BukkitRunnable() {
				public void run() {
					new ItemConstructor(getManager(), player, WarpType.LAVA);
				}
			}.runTaskLater(getManager().getPlugin(), 2L);
			return;
		}

		gamer.removeWaitTime();
		gamer.setKit(KitType.NONE);

		new BukkitRunnable() {
			public void run() {
				if (!gamer.getWarp().equals(WarpType.ONE_VS_ONE))
					getManager().getWarpTeleport().teleport(gamer.getAccount().getPlayer(), gamer.getWarp());
			}
		}.runTaskLater(getManager().getPlugin(), 2L);

		if (gamer.getWarp().equals(WarpType.LAVA)) {
			gamer.getAccount().getPlayer().sendMessage("§c§lDEATH §fVocê morreu!");
		} else if (!gamer.getWarp().equals(WarpType.NONE)) {
			gamer.getAccount().getPlayer()
					.sendMessage("§eVocê agora está na warp " + gamer.getWarp().getName() + "");
		}

		if (gamer.getWarp().equals(WarpType.NONE) || gamer.getWarp().equals(WarpType.SPAWN)) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);

			player.getInventory().clear();

			CacheItems.JOIN.build(player);

			player.updateInventory();

			getManager().getGamerManager().removePottionEffects(player);
			getManager().getGamerManager().resetPlayer(player);
			gamer.setPvP(false);
		}
	}


}
