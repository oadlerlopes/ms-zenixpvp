package br.com.zenix.pvp.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.player.inventories.KitInventory;
import br.com.zenix.pvp.player.inventories.SignInventory;
import br.com.zenix.pvp.player.inventories.WarpsInventory;
import br.com.zenix.pvp.player.inventories.KitInventory.KitInventoryMode;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class GamerInteractItemEvent extends PvPListener {

	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getClickedBlock() != null) {
			Material type = event.getClickedBlock().getType();

			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (type.equals(Material.SIGN)
					|| type.equals(Material.SIGN_POST) || type.equals(Material.WALL_SIGN))) {
				Sign sign = (Sign) event.getClickedBlock().getState();

				if (sign.getLine(2).equals("§7» §8Sopas")) {
					new SignInventory(event.getPlayer()).soup();
				} else if (sign.getLine(2).equals("§7» §8Recraft")) {
					new SignInventory(event.getPlayer()).recraft();
				}
			}
		}

		if (event.getItem() == null)
			return;

		Player player = event.getPlayer();

		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (player.getItemInHand().getType() == Material.DIAMOND && gamer.getWarp().equals(WarpType.NONE)) {
			player.sendMessage("§bAdquira o seu VIP em §b§lLOJA.ZENIX.CC");
		}

		ItemStack item = event.getItem();
		Damageable entityDamageable = player;
		if (item.getType().equals(Material.MUSHROOM_SOUP) && event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);

			if (entityDamageable.getHealth() != entityDamageable.getMaxHealth()) {
				entityDamageable.setHealth((entityDamageable.getHealth() + 7.0 > entityDamageable.getMaxHealth())
						? entityDamageable.getMaxHealth() : (entityDamageable.getHealth() + 7.0));
				player.setItemInHand(new ItemStack(Material.BOWL));
			}
		} else if (item.equals(CacheItems.JOIN.getItem(1).getStack())) {
			new WarpsInventory(player);
		} else if (item.equals(CacheItems.JOIN.getItem(0).getStack())) {
			new KitInventory(player, KitInventoryMode.YOUR_KITS, 1);
		} else if (item.equals(CacheItems.JOIN.getItem(2).getStack())) {
			player.sendMessage("§cNão há nenhum evento aberto no momento!");
		} else if (item.equals(CacheItems.COMPASS.getItem(0).getStack())) {
			event.setCancelled(true);
			boolean found = false;

			for (Entity entidades : player.getNearbyEntities(300, 100, 300)) {
				if (entidades instanceof Player) {
					Player playerFound = (Player) entidades;
					Gamer gamerFound = getManager().getGamerManager().getGamer(playerFound);

					if ((gamerFound.getWarp().equals(WarpType.NONE) && gamerFound.hasPvP()
							&& !gamerFound.getKit().equals(KitType.NONE)
							&& player.getLocation().distance(playerFound.getLocation()) > 5.0D)) {
						player.setCompassTarget(playerFound.getLocation());
						found = true;

						event.getPlayer().sendMessage("§aApontando para §f" + playerFound.getName());
					}
				}
				if (found)
					break;
			}
			if (!found) {
				player.setCompassTarget(player.getWorld().getSpawnLocation());
				player.sendMessage("§cNenhum jogador foi encontrado!");
			}
		}
	}

	@EventHandler
	private void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equals("potions")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Potions");
			event.setLine(3, "");
		} else if (event.getLine(0).equals("soups")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Sopas");
			event.setLine(3, "");
		} else if (event.getLine(0).equals("recraft")) {
			event.setLine(0, "");
			event.setLine(1, "§6Zenix");
			event.setLine(2, "§7» §8Recraft");
		}
	}
	
	@EventHandler
	private void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();

		Gamer gamer = getManager().getGamerManager().getGamer(player);
		FightMode fight1v1 = getManager().getOneVsOneManager().getFight();

		if (gamer.getWarp().equals(WarpType.LAVA)) {
			event.setCancelled(false);
			return;
		}

		if (!gamer.hasPvP()) {
			event.setCancelled(true);
			cancelPlayerDrop(player);
		}

		if (item.getType().name().contains("BUCKET")) {
			event.setCancelled(true);
		}

		if (item.hasItemMeta() && !item.getType().equals(Material.MUSHROOM_SOUP)
				&& !item.getType().equals(Material.BOWL)) {
			event.setCancelled(true);
			cancelPlayerDrop(player);
		}

		if (gamer.getWarp().equals(WarpType.NONE) && gamer.getKit().equals(KitType.NONE)) {
			event.setCancelled(true);
			cancelPlayerDrop(player);
		}
		if (gamer.getWarp().equals(WarpType.ONE_VS_ONE) && !fight1v1.isFighting(player.getUniqueId())) {
			event.setCancelled(true);
			cancelPlayerDrop(player);
		}

	}

	public void cancelPlayerDrop(Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(getManager().getPlugin(), new Runnable() {
			public void run() {
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
				player.updateInventory();
			}
		}, 1L);
	}

	@EventHandler
	public void onPlayerPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (getManager().getAdminManager().isAdmin(player)) {
			event.setCancelled(true);
			return;
		}
		if (!gamer.hasPvP()) {
			event.setCancelled(true);
			cancelPlayerDrop(event.getPlayer());
		}
	}


}
