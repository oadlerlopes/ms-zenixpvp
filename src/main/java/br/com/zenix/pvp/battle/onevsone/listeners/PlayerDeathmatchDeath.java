package br.com.zenix.pvp.battle.onevsone.listeners;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.core.plugin.data.handler.DataHandler;
import br.com.zenix.core.plugin.data.handler.type.DataType;
import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.core.spigot.player.league.player.PlayerLeague;
import br.com.zenix.pvp.battle.onevsone.managements.constructor.BattleConstructor;
import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.warps.constructor.ItemConstructor;
import br.com.zenix.pvp.warps.type.WarpType;

public class PlayerDeathmatchDeath extends PvPListener {

	private void cleanUp(final Player player) {
		new BukkitRunnable() {
			public void run() {
				player.closeInventory();
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);

				getManager().getGamerManager().resetPlayer(player);
				getManager().getGamerManager().removePottionEffects(player);
				getManager().getConfigManager().teleportPlayer(player, WarpType.ONE_VS_ONE);

				player.playSound(player.getLocation(), Sound.LEVEL_UP, 4.0F, 4.0F);

				new ItemConstructor(getManager(), player, WarpType.ONE_VS_ONE);
				getManager().getOneVsOneManager().getFight().removeFight(player.getUniqueId());
			}
		}.runTaskLater(getManager().getPlugin(), 1L);
	}

	private void lose(FightMode fightMode, Player loser, Player winner) {
		cleanUp(loser);

		Gamer gamer = getManager().getGamerManager().getGamer(loser);
		Account account = gamer.getAccount();

		DataHandler data = account.getDataHandler();
		data.getValue(DataType.PVP_DEATH).setValue(data.getValue(DataType.PVP_DEATH).getValue() + 1);
		data.getValue(DataType.PVP_LOSE).setValue(data.getValue(DataType.PVP_LOSE).getValue() + 1);
		data.getValue(DataType.PVP_KILLSTREAK).setValue(0);
		data.update(DataType.PVP_DEATH);
		data.update(DataType.PVP_LOSE);
		data.update(DataType.PVP_KILLSTREAK);
	}

	private void win(FightMode fightMode, Player winner, Player loser) {
		cleanUp(winner);

		Gamer gamer = getManager().getGamerManager().getGamer(winner);
		Account account = gamer.getAccount();

		DataHandler data = account.getDataHandler();
		data.getValue(DataType.PVP_KILL).setValue(data.getValue(DataType.PVP_KILL).getValue() + 1);
		data.getValue(DataType.PVP_WIN).setValue(data.getValue(DataType.PVP_WIN).getValue() + 1);
		data.getValue(DataType.PVP_KILLSTREAK).setValue(data.getValue(DataType.PVP_KILLSTREAK).getValue() + 1);
		data.update(DataType.PVP_KILL);
		data.update(DataType.PVP_WIN);
		data.update(DataType.PVP_KILLSTREAK);
		
		getManager().getLogger().log("O player " + winner.getName() + " ganhou o 1v1 de " + loser.getName() + "!");
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {

			if (getManager().getGamerManager().getGamer(event.getEntity().getUniqueId()).getWarp()
					.equals(WarpType.ONE_VS_ONE)
					&& getManager().getGamerManager().getGamer(event.getEntity().getKiller().getUniqueId()).getWarp()
							.equals(WarpType.ONE_VS_ONE)) {

				DecimalFormat dm = new DecimalFormat("##.#");
				final Player loser = event.getEntity();
				final Player winner = loser.getKiller();

				int i = 0;
				for (ItemStack soup : winner.getInventory().getContents()) {
					if ((soup != null) && (soup.getType() != Material.AIR)
							&& (soup.getType() == Material.MUSHROOM_SOUP)) {
						i += soup.getAmount();
					}
				}

				loser.sendMessage("§c§lDEATH §fVocê morreu para §c§l" + winner.getName() + "§f com "
						+ dm.format(winner.getHealth() / 2.0D) + " coracoes e " + i + " sopas restantes.");
				winner.sendMessage("§e§lKILL §fVocê matou §e§l" + loser.getName() + "§f com "
						+ dm.format(winner.getHealth() / 2.0D) + " coracoes e " + i + " sopas restantes.");

				for (ItemStack itemStack : event.getDrops()) {
					itemStack.setType(Material.AIR);
				}

				FightMode fightMode = getManager().getOneVsOneManager().getFight();

				Account accountWinner = getManager().getCoreManager().getAccountManager().getAccount(winner);
				Account accountLoser = getManager().getCoreManager().getAccountManager().getAccount(loser);

				new PlayerLeague(winner, loser).prizeLeague();

				win(fightMode, winner, loser);
				
				DataHandler dataHandlerFirst = accountLoser.getDataHandler();

				if (accountLoser != null) {
					if (dataHandlerFirst.getValue(DataType.PVP_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + accountLoser.getPlayer().getName()
								+ "§f perdeu seu §6§lKILLSTREAK DE "
								+ dataHandlerFirst.getValue(DataType.PVP_KILLSTREAK).getValue() + "§f para §c§l"
								+ accountWinner.getPlayer().getName() + "§f");
					}
				}

				lose(fightMode, loser, winner);

				controlPlayer(winner, loser);

				DataHandler dataHandler = accountWinner.getDataHandler();

				if (dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() % 10 == 0) {
					if (dataHandler.getValue(DataType.PVP_KILLSTREAK).getValue() >= 10) {
						Bukkit.broadcastMessage("§4§lKILLSTREAK §1§l" + accountWinner.getPlayer().getName()
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

				new PlayerLeague(winner).checkRank(accountWinner);
				new PlayerLeague(loser).checkRank(accountLoser);
			}

		} else {
			FightMode fightMode = getManager().getOneVsOneManager().getFight();

			if (!getManager().getGamerManager().getGamer(event.getEntity()).getWarp().equals(WarpType.ONE_VS_ONE)
					|| !fightMode.isFighting(event.getEntity().getUniqueId())) {
				return;
			}

			BattleConstructor battleConstructor = fightMode.getBattle(event.getEntity().getUniqueId());
			Player winner = battleConstructor.getOpponent();

			cleanUp(event.getEntity());
			cleanUp(winner);

			controlPlayer(winner, event.getEntity());

			new PlayerLeague(winner, event.getEntity()).prizeLeague();

			winner.sendMessage("§aO seu oponente se matou, você ganhou esse 1v1.");

			for (Player players : Bukkit.getOnlinePlayers()) {
				if (getManager().getOneVsOneManager().getFight().isFighting(players.getUniqueId())) {
					players.hidePlayer(winner);
				}
				if (getManager().getGamerManager().getGamer(players).isSpectate()) {
					players.hidePlayer(winner);
				}
			}
		}
	}

	private void controlPlayer(final Player winner, final Player loser) {
		new BukkitRunnable() {
			public void run() {
				FightMode fightMode = getManager().getOneVsOneManager().getFight();

				for (Player online : Bukkit.getOnlinePlayers()) {
					if (!fightMode.isFighting(online.getUniqueId())
							&& !getManager().getGamerManager().getGamer(online).isSpectate()) {
						online.showPlayer(winner);
						online.showPlayer(loser);
					}
					if (!getManager().getAdminManager().isAdmin(online)) {
						winner.showPlayer(online);
						loser.showPlayer(online);
					}

					if (getManager().getGamerManager().getGamer(online).isSpectate()) {
						winner.hidePlayer(online);
						loser.hidePlayer(online);
					}

				}
			}
		}.runTaskLater(getManager().getPlugin(), 4L);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Player loser = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(loser);
		FightMode fightMode = getManager().getOneVsOneManager().getFight();

		if (!gamer.getWarp().equals(WarpType.ONE_VS_ONE) || !fightMode.isFighting(loser.getUniqueId())) {
			return;
		}

		BattleConstructor battleConstructor = fightMode.getBattle(loser.getUniqueId());
		Player winner = battleConstructor.getOpponent();

		cleanUp(loser);
		cleanUp(winner);

		Account account = getManager().getGamerManager().getGamer(winner).getAccount();

		DataHandler data2 = getManager().getGamerManager().getGamer(loser).getAccount().getDataHandler();
		data2.getValue(DataType.PVP_KILLSTREAK).setValue(0);
		data2.update(DataType.PVP_KILLSTREAK);
		
		DataHandler data = account.getDataHandler();
		data.getValue(DataType.PVP_KILL).setValue(data.getValue(DataType.PVP_KILL).getValue() + 1);
		data.getValue(DataType.PVP_WIN).setValue(data.getValue(DataType.PVP_WIN).getValue() + 1);
		data.getValue(DataType.PVP_KILLSTREAK).setValue(data.getValue(DataType.PVP_KILLSTREAK).getValue() + 1);
		data.update(DataType.PVP_KILL);
		data.update(DataType.PVP_WIN);
		data.update(DataType.PVP_KILLSTREAK);

		getManager().getGamerManager().getGamer(winner).update();
		getManager().getGamerManager().getGamer(loser).update();

		getManager().getGamerManager().getGamer(loser).getAccount().getDataHandler().getValue(DataType.PVP_KILLSTREAK).setValue(0);
		getManager().getGamerManager().getGamer(loser).getAccount().getDataHandler().update(DataType.PVP_KILLSTREAK);
		
		Random random = new Random();
		int r = random.nextInt(15);
		int debit = r;

		if (account.isDoubleRunning()) {
			if (data.getValue(DataType.PVP_KILLSTREAK).getValue() >= 70) {
				debit = (((data.getValue(DataType.PVP_KILLSTREAK).getValue() * 2) / 3) + r);
			} else {
				debit = (((data.getValue(DataType.PVP_KILLSTREAK).getValue() * 2) / 2) + r);
			}
			data.getValue(DataType.GLOBAL_XP).setValue(data.getValue(DataType.GLOBAL_XP).getValue() + debit);
			data.getValue(DataType.GLOBAL_XP);

			account.getPlayer().sendMessage("§9§lXP §fVocê ganhou §9§l" + debit + "XPs §7(DoubleXP)");
		} else {
			if (data.getValue(DataType.PVP_KILLSTREAK).getValue() == 0) {
				debit = (((4 * 2) / 2) + r);
			}
			data.getValue(DataType.GLOBAL_XP).setValue(data.getValue(DataType.GLOBAL_XP).getValue() + debit);
			data.getValue(DataType.GLOBAL_XP);

			account.getPlayer().sendMessage("§9§lXP §fVocê ganhou §9§l" + debit + "XPs");
		}

		controlPlayer(winner, loser);
		winner.sendMessage("§2§lVITÓRIA §fSeu oponente §2§lDESISTIU§f de lutar, portanto você §a§lVENCEU§f a batalha.");

		for (Player all : Bukkit.getOnlinePlayers()) {
			if (!fightMode.isFighting(all.getUniqueId()))
				all.showPlayer(winner);
		}

	}

}
