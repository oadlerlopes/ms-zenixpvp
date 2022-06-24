package br.com.zenix.pvp.battle.onevsone.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.zenix.pvp.battle.onevsone.managements.RunnableStartingMatch;
import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.battle.onevsone.modules.BattleModules;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;
import br.com.zenix.pvp.warps.type.WarpType;

public class PlayerInviteToMatch extends PvPListener {

	public static ArrayList<UUID> fast = new ArrayList<>();

	public static ArrayList<UUID> getFastPlayers() {
		return fast;
	}

	@EventHandler
	private void onInteract(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof Player))
			return;

		Player player = event.getPlayer();
		Player player2 = (Player) event.getRightClicked();

		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE)) {

			ItemStack item = event.getPlayer().getItemInHand();

			if (item.getType().equals(Material.BLAZE_ROD) || item.getType().equals(Material.PAPER)
					|| item.getType().equals(Material.BONE)) {

				FightMode fightMode = getManager().getOneVsOneManager().getFight();

				if (fightMode.isFighting(player.getUniqueId())
						|| !getManager().getGamerManager().getGamer(player2).getWarp().equals(WarpType.ONE_VS_ONE))
					return;

				if (fightMode.isFighting(player2.getUniqueId())) {
					player.sendMessage("§cO jogador já está lutando!");
					return;
				}

				if (fightMode.recivedInvitation(player.getUniqueId(), player2.getUniqueId())) {
					BattleModules battleModules = fightMode.getInvitation(player2.getUniqueId()).getBattleType();
					if ((battleModules.equals(BattleModules.NORMAL) && item.getType().equals(Material.BLAZE_ROD))) {
						fast.remove(player.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para 1v1 §7(Clique)").setDurability(8)
								.build(player.getInventory(),
										getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));
						fast.remove(player2.getUniqueId());
						new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para 1v1 §7(Clique)").setDurability(8)
								.build(player2.getInventory(),
										getManager().getUtils().findItem(player2.getInventory(), Material.INK_SACK));
						new RunnableStartingMatch(player, player2,
								fightMode.getInvitations().get(player2.getUniqueId()).getBattleType());
						return;
					}
				}

				if (gamer.inWaitTime()) {
					player.sendMessage("§cAguarde para poder convidar novamente.");
					return;
				}

				BattleModules battleModules = item.getType().equals(Material.BLAZE_ROD) ? BattleModules.NORMAL
						: BattleModules.NORMAL;

				fightMode.addInvitation(player, player2, battleModules);
				player.sendMessage(
						"§eVocê desafiou o jogador " + player2.getName() + " para uma batalha!");
				player2.sendMessage("§bO jogador " + player.getName() + " te desafiou para uma batalha.");

				gamer.addWait(6);
			}

		}
	}
	
	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Gamer gamer = getManager().getGamerManager().getGamer(player);
		final ItemStack item = player.getItemInHand();

		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE)
				&& !getManager().getOneVsOneManager().getFight().isFighting(player.getUniqueId())) {

			if (!fast.contains(player.getUniqueId()) && item.getType().equals(Material.INK_SACK)
					&& item.getDurability() == 8) {
				item.setDurability((short) 10);
				fast.add(player.getUniqueId());

				player.sendMessage("§9Você agora está esperando uma partida rápida.");

				new BukkitRunnable() {
					int time;

					public void run() {

						if (getManager().getOneVsOneManager().getFight().isFighting(player.getUniqueId())
								|| !gamer.getWarp().equals(WarpType.ONE_VS_ONE)
								|| !containsItem(player.getInventory(), Material.INK_SACK, 10)
								|| !fast.contains(player.getUniqueId())) {
							fast.remove(player.getUniqueId());
							cancel();
							return;
						}

						if (time == 10 && fast.contains(player.getUniqueId())
								&& !getManager().getOneVsOneManager().getFight().isFighting(player.getUniqueId())
								&& gamer.getWarp().equals(WarpType.ONE_VS_ONE)) {
							fast.remove(player.getUniqueId());

							new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para 1v1 §7(Clique)")
									.setDurability(8).build(player.getInventory(),
											getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));

							player.sendMessage(
									"§a§lSEARCH §fNão encontramos ninguém! A busca por partidas foi cancelada!");
							cancel();
							return;
						}

						time++;

						new ItemBuilder(Material.AIR).chanceItemStack(item).setName("§aDesafiar para 1v1 §7(Clique)")
								.build(player.getInventory(),
										getManager().getUtils().findItem(player.getInventory(), item.getType()));
					}
				}.runTaskTimerAsynchronously(getManager().getPlugin(), 0L, 20L);

				if (fast.size() == 2) {
					new RunnableStartingMatch(Bukkit.getPlayer(fast.get(0)), Bukkit.getPlayer(fast.get(1)),
							BattleModules.FAST);
					fast.clear();
				}

			} else if (fast.contains(player.getUniqueId()) && item.getType().equals(Material.INK_SACK)
					&& item.getDurability() == 10) {
				fast.remove(player.getUniqueId());
				new ItemBuilder(Material.INK_SACK).setName("§aDesafiar para 1v1 §7(Clique)").setDurability(8).build(
						player.getInventory(),
						getManager().getUtils().findItem(player.getInventory(), Material.INK_SACK));
				player.sendMessage("§cVocê não está mais aguardando uma partida na fila rápida.");
			}

			return;
		}
	}

	private boolean containsItem(Inventory inventory, Material material, int durability) {
		boolean contains = false;
		for (ItemStack itemStack : inventory.getContents()) {
			if (itemStack != null && itemStack.getType().equals(material) && itemStack.getDurability() == durability) {
				contains = true;
			}
		}
		return contains;
	}

}
