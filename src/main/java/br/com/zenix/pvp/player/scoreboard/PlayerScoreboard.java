package br.com.zenix.pvp.player.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.data.handler.type.DataType;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.scoreboard.ScoreboardConstructor;
import br.com.zenix.core.spigot.player.scoreboard.ScoreboardScroller;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class PlayerScoreboard extends Management {

	private String title = "§e§lZENIX";
	private ScoreboardScroller scoreboardScroller;
	
	public PlayerScoreboard(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		scoreboardScroller = new ScoreboardScroller("     ZENIX     ", "§f§l", "§6§l", "§6§l", 8);
		return startScores();
	}

	public boolean startScores() {

		new BukkitRunnable() {
			public void run() {

				if (Bukkit.getOnlinePlayers().size() == 0) {
					return;
				}

				title = "§f§l" + scoreboardScroller.next();

				for (Player player : Bukkit.getOnlinePlayers()) {
					updateScoreboard(player);
				}

			}
		}.runTaskTimer(getManager().getPlugin(), 2, 2);
		return true;
	}

	@SuppressWarnings("unused")
	public void createScoreboard(Player player) {
		ScoreboardConstructor scoreboardHandler = new ScoreboardConstructor(player);

		Gamer gamer = getManager().getGamerManager().getGamer(player);
		Account account = gamer.getAccount();

		if (gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		scoreboardHandler.initialize(title);

		scoreboardHandler.setScore("§b§c", "§2§l", "§f");
		if (gamer.getWarp() != WarpType.LAVA) {
			scoreboardHandler.setScore("§fKills ", "", "0");
			scoreboardHandler.setScore("§fDeaths ", "", "0");
			scoreboardHandler.setScore("§fKillStreak ", "", "0");
			scoreboardHandler.setScore("§7§c", "§2§l", "§f");
			scoreboardHandler.setScore("§fLiga ", "", "§f- UNRANKED");
			if (gamer.getWarp().equals(WarpType.ONE_VS_ONE)) {
				scoreboardHandler.setScore("§3§c", "§2§l", "§f");
				scoreboardHandler.setScore("§fBatalhando ", "", "§fcontra:");
				scoreboardHandler.setScore("§a", "", "§aNinguém");

			} else if (gamer.getWarp() != WarpType.ONE_VS_ONE) {
				if (gamer.getWarp() == WarpType.FPS) {
					scoreboardHandler.setScore("§3§c", "§2§l", "§f");
					scoreboardHandler.setScore("§fWarp atual §3", "", "§3" + gamer.getWarp().getName());
				} else if (gamer.getWarp().equals(WarpType.NONE) || gamer.getWarp().equals(WarpType.SPAWN)) {
					scoreboardHandler.setScore("§3§c", "§2§l", "§f");
					scoreboardHandler.setScore("§fKit atual §a", "", "§3");
				}
			} else {
				scoreboardHandler.setScore("§fBatalhando ", "", "§fcontra:");
				scoreboardHandler.setScore("§a", "", "§aNinguém");
			}
		}
		scoreboardHandler.setScore("§a§2", "§3§c", "§8");
		scoreboardHandler.setScore("pvp.zenix", "§6", "§6.cc");

		account.setScoreboardHandler(scoreboardHandler);
	}

	public void updateScoreboard(Player player) {

		Gamer gamer = getManager().getGamerManager().getGamer(player);
		Account account = gamer.getAccount();

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
		}

		if (account.getScoreboardHandler() == null) {
			createScoreboard(player);
		}

		ScoreboardConstructor scoreboardHandler = account.getScoreboardHandler();

		scoreboardHandler.setDisplayName(title);

		if (account.getRank().getName() == null || account.getLeague().getName() == null || gamer == null) {
			player.kickPlayer("§4§lERRO §fA sua conta está com problemas!");
			return;
		}

		scoreboardHandler.updateScore("§fKills ", "§f",
				"§a" + account.getDataHandler().getValue(DataType.PVP_KILL).getValue());
		scoreboardHandler.updateScore("§fDeaths ", "§f",
				"§a" + account.getDataHandler().getValue(DataType.PVP_DEATH).getValue());
		scoreboardHandler.updateScore("§fKillStreak ", "§f",
				"§a" + account.getDataHandler().getValue(DataType.PVP_KILLSTREAK).getValue());
		scoreboardHandler.updateScore("§fXP ", "",
				"§a" + account.getDataHandler().getValue(DataType.GLOBAL_XP).getValue());
		scoreboardHandler.updateScore("§fLiga ", "", "" + account.getLeague().getColor()
				+ account.getLeague().getSymbol() + " " + account.getLeague().getName().toUpperCase());
		
		scoreboardHandler.updateScore("§fRank ", "§f",
				"" + gamer.getAccount().getRank().getTag().getColor().replace("§l", "")
						+ getManager().getUtils().captalize(gamer.getAccount().getRank().getName()));
		
		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE)) {
			scoreboardHandler.updateScore("§a", "", "§a" + getManager().getOneVsOneManager().getFighter(player));
		} else {
			if (gamer.getKit().getName() == null) {
				player.kickPlayer("Ocorreu um problema ao carregar seu kit.");
			}
			scoreboardHandler.updateScore("§fKit atual §a", "", "§a" + gamer.getKit().getName());
		}
	}
}
