package br.com.zenix.pvp.warps;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.commands.base.MessagesConstructor;
import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.warps.constructor.ItemConstructor;
import br.com.zenix.pvp.warps.type.WarpType;

public class WarpManager extends Management {

	public WarpManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public void teleport(Player player, WarpType warp) {
		getLogger().log("The player " + player.getName() + " has loaded to teleport");

		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (gamer.inCombat())
			return;

		if (warp.equals(WarpType.ONE_VS_ONE)) {
			gamer.setWarp(warp);

			gamer.setKit(KitType.NONE);
			getManager().getGamerManager().resetPlayer(player);
			getManager().getGamerManager().removePottionEffects(player);
			getManager().getConfigManager().teleportPlayer(player, WarpType.ONE_VS_ONE);

			MessagesConstructor.sendTitleMessage(player, "§61v1", "Desafie jogadores para um 1v1!");

			new ItemConstructor(getManager(), player, warp);
			getLogger().log("The player " + player.getName() + " has complete teleport");
		} else if (warp.equals(WarpType.FPS)) {
			gamer.setWarp(warp);

			gamer.setKit(KitType.NONE);
			gamer.setPvP(false);
			gamer.setWarp(WarpType.FPS);
			getManager().getGamerManager().resetPlayer(player);
			getManager().getGamerManager().removePottionEffects(player);
			getManager().getConfigManager().teleportPlayer(player, WarpType.FPS);

			MessagesConstructor.sendTitleMessage(player, "§bFPS", "Treine seu PvP e derrote seus adversários!");

			new ItemConstructor(getManager(), player, warp);
		} else if (warp.equals(WarpType.LAVA)) {
			gamer.setWarp(warp);

			gamer.setKit(KitType.NONE);
			gamer.setPvP(false);
			gamer.setWarp(WarpType.LAVA);
			getManager().getGamerManager().resetPlayer(player);
			getManager().getGamerManager().removePottionEffects(player);
			getManager().getConfigManager().teleportPlayer(player, WarpType.LAVA);

			MessagesConstructor.sendTitleMessage(player, "§6Lava", "Treine seu refil e recraft!");

			new ItemConstructor(getManager(), player, warp);
			gamer.setPvP(false);
		} else {
			gamer.setWarp(WarpType.NONE);
			gamer.setKit(KitType.NONE);
			gamer.setPvP(false);

			player.setFireTicks(0);

			player.getInventory().clear();

			getManager().getGamerManager().resetPlayer(player);
			getManager().getGamerManager().removePottionEffects(player);

			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());

			CacheItems.JOIN.build(player);
		}

		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 4.0F, 4.0F);
		player.setLevel(0);

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getManager().getOneVsOneManager().getFight().isFighting(players.getUniqueId())) {
				players.hidePlayer(player);
			}
			if (getManager().getGamerManager().getGamer(players).isSpectate()) {
				players.hidePlayer(player);
			}
		}

	}

	public void teleportPlayer(final Player player, final WarpType warp) {
		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE) && new FightMode().isFighting(player.getUniqueId()))
			return;

		if (gamer.inCombat())
			return;

		if (gamer.getWarp().equals(warp)) {
			teleport(player, WarpType.SPAWN);
			return;
		}

		teleport(player, warp);
		sendMessage(player, warp);

		getManager().getCoreManager().getAccountManager().getAccount(player).setScoreboardHandler(null);
		getManager().getCoreManager().getAccountManager().getAccount(player).setScoreboardHandler(null);
	}

	private void sendMessage(Player player, WarpType warp) {
		player.sendMessage("§eVocê agora está na warp §c" + warp.getName());

	}

}
