package br.com.zenix.pvp.battle.onevsone.managements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.battle.onevsone.listeners.PlayerInviteToMatch;
import br.com.zenix.pvp.battle.onevsone.managements.custom.Custom;
import br.com.zenix.pvp.battle.onevsone.modules.BattleModules;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.utilitaries.Variables;
import br.com.zenix.pvp.utilitaries.armor.ArmorUtilitaries;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;
import br.com.zenix.pvp.warps.type.WarpType;

public class RunnableStartingMatch {

	private Manager manager;

	private Player player1, player2;

	private BattleModules battleModules;

	public RunnableStartingMatch() {
		this.manager = PvP.getManager();
	}

	public RunnableStartingMatch(Player player1, Player player2, BattleModules battleModules) {
		this.manager = PvP.getManager();
		this.player1 = player1;
		this.player2 = player2;
		this.battleModules = battleModules;
		start();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player1.hidePlayer(player);
			player2.hidePlayer(player);
			if (manager.getOneVsOneManager().getFight().isFighting(player.getUniqueId())) {
				player.hidePlayer(player1);
				player.hidePlayer(player2);
			}
			if (manager.getGamerManager().getGamer(player).isSpectate()) {
				player1.hidePlayer(player);
				player2.hidePlayer(player);
				player.hidePlayer(player1);
				player.hidePlayer(player2);
			}
		}

		player2.showPlayer(player1);
		player1.showPlayer(player2);

		player2.showPlayer(player1);
		player1.showPlayer(player2);
	}

	private void prepare(Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		manager.getOneVsOneManager().getFight().removeInvitation(player);

		PvP.getManager().getGamerManager().resetPlayer(player);
	}

	private Custom getCustom() {
		return (manager.getOneVsOneManager().getFight().customized(player1)
				? manager.getOneVsOneManager().getFight().getCustom(player1)
				: manager.getOneVsOneManager().getFight().getCustom(player2));
	}

	private int getSoups() {
		return battleModules.getSoups();
	}

	private void giveItems(Player player) {
		if (battleModules.equals(BattleModules.NORMAL) || battleModules.equals(BattleModules.FAST)) {
			if (Variables.FULL_IRON) {
				CacheItems.DEFAULT_SWORD.getItem(0).setMaterial(Material.DIAMOND_SWORD).setUnbreakable()
						.setEnchant(Enchantment.DAMAGE_ALL, 1).setName("§bEspada de diamante").build(player);
				manager.getGamerManager().fillInventory(player, CacheItems.SOUP, (battleModules.getSoups() + 1));
				ArmorUtilitaries.updateArmor(player, "IRON", false);
			} else {
				new ItemBuilder(Material.WOOD_SWORD).setEnchant(Enchantment.DAMAGE_ALL, 1).setUnbreakable()
				.setName("§b§lWood Sword").build(player);
				manager.getGamerManager().fillInventory(player, CacheItems.SOUP, (battleModules.getSoups() + 1));
			}
		} else if (battleModules.equals(BattleModules.CUSTOM)) {
			Custom custom = getCustom();
			ItemBuilder sword = new ItemBuilder(Material.AIR).chanceItemStack(new ItemStack(custom.getSword()))
					.setUnbreakable().setName("§bEspada");

			if (custom.sharp())
				sword.setEnchant(Enchantment.DAMAGE_ALL, 1);

			sword.build(player);

			if (custom.recraft())
				CacheItems.RECRAFT.build(player);

			manager.getGamerManager().fillInventory(player, CacheItems.SOUP, (custom.recraft() ? 36 : 8));

			if (custom.getArmor() != null)
				if (custom.getArmor().name().contains("_CHESTPLATE"))
					ArmorUtilitaries.updateArmor(player, custom.getArmor().name().replace("_CHESTPLATE", ""), false);
		}
	}

	private void sendMessage(Player player, Player p2) {
		player.sendMessage("");
		player.sendMessage("§aVocê está batalhando com o jogador §f" + p2.getName() + "§a!");
		player.sendMessage("§aTipo de batalha: §f" + battleModules);
		player.sendMessage("");
	}

	private void start() {

		if (battleModules.equals(BattleModules.NORMAL) || battleModules.equals(BattleModules.FAST)
				|| battleModules.equals(BattleModules.CUSTOM)) {
			
			player1.closeInventory();
			player2.closeInventory();

			manager.getOneVsOneManager().getFight().addFight(player1, player2, battleModules, getSoups());

			player1.closeInventory();
			player2.closeInventory();
			
			player1.getInventory().clear();
			player2.getInventory().clear();
			
			prepare(player1);
			prepare(player2);
			giveItems(player1);
			giveItems(player2);
			sendMessage(player1, player2);
			sendMessage(player2, player1);

			manager.getConfigManager().teleportPlayer(player1, WarpType.ONE_VS_ONE, "1v1.pos1");
			manager.getConfigManager().teleportPlayer(player2, WarpType.ONE_VS_ONE, "1v1.pos2");

			for (Player player : Bukkit.getOnlinePlayers()) {
				player1.hidePlayer(player);
				player2.hidePlayer(player);
				if (manager.getOneVsOneManager().getFight().isFighting(player.getUniqueId())) {
					player.hidePlayer(player1);
					player.hidePlayer(player2);
				}
			}

			player2.showPlayer(player1);
			player1.showPlayer(player2);

			player2.showPlayer(player1);
			player1.showPlayer(player2);

			PlayerInviteToMatch.getFastPlayers().remove(player1.getUniqueId());
			PlayerInviteToMatch.getFastPlayers().remove(player2.getUniqueId());
			
			for (Player player : Bukkit.getOnlinePlayers()) {
				player1.hidePlayer(player);
				player2.hidePlayer(player);
				if (manager.getOneVsOneManager().getFight().isFighting(player.getUniqueId())) {
					player.hidePlayer(player1);
					player.hidePlayer(player2);
				}
				if (manager.getGamerManager().getGamer(player).isSpectate()) {
					player1.hidePlayer(player);
					player2.hidePlayer(player);
					player.hidePlayer(player1);
					player.hidePlayer(player2);
				}
			}

			player2.showPlayer(player1);
			player1.showPlayer(player2);

			player2.showPlayer(player1);
			player1.showPlayer(player2);
			
			
			PvP.getManager().getGamerManager().getGamer(player1).setPvP(true);
			PvP.getManager().getGamerManager().getGamer(player2).setPvP(true);

			return;
		}

	}

}
