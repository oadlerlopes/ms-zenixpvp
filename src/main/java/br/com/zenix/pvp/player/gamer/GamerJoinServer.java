package br.com.zenix.pvp.player.gamer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;

import br.com.zenix.core.spigot.commands.base.MessagesConstructor;
import br.com.zenix.core.spigot.player.events.ServerTimeEvent;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.utilitaries.item.CacheItems;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */

public class GamerJoinServer extends PvPListener {

	@EventHandler
	public void onServerGamer(ServerTimeEvent event) {
		for (Player players : Bukkit.getOnlinePlayers()) {
			getManager().getGamerManager().updateTab(getManager().getGamerManager().getGamer(players));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		player.teleport(Bukkit.getWorld("world").getSpawnLocation());

		getManager().getGamerManager().removePottionEffects(player);
		getManager().getGamerManager().resetPlayer(player);

		getManager().getGamerManager().updateTab(gamer);

		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		player.updateInventory();
		player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 4.0F, 4.0F);

		CacheItems.JOIN.build(player);

		StringBuilder stringTitle = new StringBuilder();
		stringTitle.append("§e§lZENIX PVP");

		StringBuilder stringSubTitle = new StringBuilder();
		stringSubTitle.append("§6PvP Server : §fzenix.cc");

		MessagesConstructor.sendTitleMessage(player, stringTitle.toString(), stringSubTitle.toString());

		player.sendMessage(" ");
		player.sendMessage("§6§lZENIXPVP");
		player.sendMessage(" ");
		player.sendMessage("§fEscolha seu kit clicando no §e§lBAU§f da sua §e§lMAO §f");
		player.sendMessage("§fe §e§LBATALHE§f contra outros §e§lJOGADORES §f");
		player.sendMessage(" ");
		player.sendMessage("§9§lTENHA UM BOM JOGO!");
		player.sendMessage(" ");
		
		getManager().getGamerManager().removePottionEffects(player);
		getManager().getGamerManager().resetPlayer(player);

		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		CacheItems.JOIN.build(player);

		if (!getManager().getAdminManager().isAdmin(player)) {
			if (player.hasPermission("commons.cmd.moderate")) {
				player.chat("/admin");
			}
		}

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (getManager().getAdminManager().isAdmin(players)) {
				if (!player.hasPermission("commons.admin")) {
					player.hidePlayer(players);
				}
			}
			if (getManager().getOneVsOneManager().getFight().isFighting(players.getUniqueId())) {
				players.hidePlayer(player);
			}
			if (getManager().getGamerManager().getGamer(players).isSpectate()) {
				players.hidePlayer(player);
			}
		}
	}
}
