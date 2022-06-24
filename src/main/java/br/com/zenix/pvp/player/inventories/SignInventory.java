package br.com.zenix.pvp.player.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.zenix.pvp.utilitaries.item.CacheItems;

public class SignInventory {

	private Player player;
	private Inventory inventory;

	public SignInventory(Player player) {
		this.player = player;
	}

	public void soup() {
		inventory = Bukkit.createInventory(player, 36, "Sopas");

		for (int i = 0; i <= 36; i++) {
			CacheItems.SOUP.build(inventory);
		}

		player.openInventory(inventory);
	}

	public void recraft() {
		inventory = Bukkit.createInventory(player, 36, "Recraft");

		for (int i = 0; i <= 12; i++) {
			inventory.addItem(CacheItems.RECRAFT.getItem(0).getStack());
			inventory.addItem(CacheItems.RECRAFT.getItem(1).getStack());
			inventory.addItem(CacheItems.RECRAFT.getItem(2).getStack());
		}

		player.openInventory(inventory);
	}

}
