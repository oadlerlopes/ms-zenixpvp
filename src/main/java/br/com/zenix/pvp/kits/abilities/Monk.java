package br.com.zenix.pvp.kits.abilities;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class Monk extends PvPListener {

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			Player clicked = (Player) e.getRightClicked();
			Player player = e.getPlayer();
			
			ItemStack i = player.getItemInHand();
			
			if (i.getType() != Material.BLAZE_ROD) {
				return;
			}
			
			Gamer playerData = getManager().getGamerManager().getGamer(player);

			if (!playerData.getKit().equals(KitType.MONK))
				return;
			
			if (getManager().getKitManager().inCooldown(player)) {
				getManager().getKitManager().sendCooldown(player);
				return;
			}
			
			getManager().getKitManager().addCooldown(player, 5);

			int randomNumber = new Random().nextInt(36);

			ItemStack atual = (clicked.getItemInHand() != null ? clicked.getItemInHand().clone() : null);
			ItemStack random = (clicked.getInventory().getItem(randomNumber) != null ? clicked.getInventory().getItem(randomNumber).clone() : null);
			
			if (random == null) {
				clicked.getInventory().setItem(randomNumber, atual);
				clicked.setItemInHand(null);
			} else {
				clicked.getInventory().setItem(randomNumber, atual);
				clicked.getInventory().setItemInHand(random);
			}
			
			player.sendMessage("§6§lMONK §fO player §e§l" + clicked.getName() + "§f foi monkado com sucesso.");
		}
	}

}
