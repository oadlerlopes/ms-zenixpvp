package br.com.zenix.pvp.utilitaries.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public enum CacheItems {

	JOIN(new ItemBuilder[] { new ItemBuilder(Material.ENDER_CHEST).setName("§b§lKits §7(Clique para abrir)"), 
			new ItemBuilder(Material.COMPASS).setName("§b§lWarps §7(Clique para abrir)"),
			new ItemBuilder(Material.ENCHANTED_BOOK).setName("§b§lEventos §7(Clique para abrir)"),
			new ItemBuilder(Material.DIAMOND).setName("§b§lLoja §7(Clique para abrir)")},  new Integer[] { 1, 2, 6,7 }),
	SOUP(new ItemBuilder[] { new ItemBuilder(Material.MUSHROOM_SOUP).setName("§b§lSopa").setBreakable() }, null),
	RECRAFT(new ItemBuilder[] { new ItemBuilder(Material.RED_MUSHROOM).setAmount(64).setBreakable(), new ItemBuilder(Material.BROWN_MUSHROOM).setAmount(64).setBreakable(),new ItemBuilder(Material.BOWL).setAmount(64).setBreakable() }, new Integer[] { 13, 14, 15 }),
	RECRAFT2(new ItemBuilder[] { new ItemBuilder(Material.BLAZE_POWDER).setAmount(64).setBreakable(), new ItemBuilder(Material.SPIDER_EYE).setAmount(64).setBreakable() }, new Integer[] { 13,14 }),
	POTION_RECRAFT(new ItemBuilder[] { new ItemBuilder(Material.BLAZE_POWDER).setAmount(64).setBreakable(), new ItemBuilder(Material.SPIDER_EYE).setAmount(64).setBreakable(),new ItemBuilder(Material.SPIDER_EYE).setAmount(64).setBreakable() }, new Integer[] { 13, 14, 15 }),
	WARPS(new ItemBuilder[] { new ItemBuilder(Material.BLAZE_ROD).setName("§e§l1v1").setBreakable(), new ItemBuilder(Material.GLASS).setName("§e§lFps").setBreakable(), new ItemBuilder(Material.LAVA).setName("§e§lChallenge").setBreakable() }, new Integer[] { 10 }),
	DEFAULT_SWORD(new ItemBuilder[] { new ItemBuilder(Material.DIAMOND_SWORD).setName("§b§lDiamond Sword").setUnbreakable() }, null),
	COMPASS(new ItemBuilder[] { new ItemBuilder(Material.COMPASS).setName("§b§lBússola") }, new Integer[] { 8 });

	private ItemBuilder[] items;
	private Integer[] slots;

	CacheItems(ItemBuilder[] items, Integer[] slots) {
		this.items = items;
		this.slots = slots;
	}

	public ItemBuilder[] getItems() {
		return items;
	}

	public ItemBuilder getItem(int id) {
		return id <= items.length - 1 ? items[id] : items[0];
	}

	public Integer[] getSlots() {
		return slots;
	}

	public void build(Inventory inventory) {
		for (int i = 0; i < items.length; i++) {
			if (slots == null)
				getItem(i).build(inventory);
			else
				getItem(i).build(inventory, slots[i]);
		}
	}

	public void build(Player player) {
		build(player.getInventory());
	}

	public void build(Gamer gamer) {
		build(gamer.getAccount().getPlayer().getInventory());
	}

}
