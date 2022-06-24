package br.com.zenix.pvp.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.commands.base.MessagesConstructor;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;
import br.com.zenix.pvp.utilitaries.Variables;
import br.com.zenix.pvp.utilitaries.armor.ArmorUtilitaries;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class KitManager extends Management {

	private ConcurrentHashMap<UUID, Long> cooldown = new ConcurrentHashMap<>();

	public KitManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public void giveKit(Player player, KitType kitType, boolean simulator) {
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setExp(0.0f);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.closeInventory();

		player.sendMessage("§b§lKITS §fVocê selecionou o kit §3§l" + kitType.getName().toUpperCase());

		MessagesConstructor.sendTitleMessage(player, "§3§l" + kitType.getName(), "selecionado!");

		getManager().getGamerManager().getGamer(player).setKit(kitType);

		if (!simulator) {
			if (Variables.FULL_IRON) {

				if (kitType.equals(KitType.PVP)) {
					new ItemBuilder(Material.DIAMOND_SWORD).setEnchant(Enchantment.DAMAGE_ALL, 1).setName("§cPvP Sword")
							.setUnbreakable().build(player);
				} else {
					new ItemBuilder(Material.DIAMOND_SWORD).setName("§b§lDiamond Sword").setUnbreakable().build(player);
				}

				ArmorUtilitaries.updateArmor(player, "IRON", true);
			} else {
				if (kitType.equals(KitType.PVP)) {
					new ItemBuilder(Material.WOOD_SWORD).setEnchant(Enchantment.DAMAGE_ALL, 1).setUnbreakable()
							.setName("§b§lWood Sword").build(player);
				} else {
					new ItemBuilder(Material.WOOD_SWORD).setName("§b§lWood Sword").setUnbreakable().build(player);
				}
			}

			CacheItems.RECRAFT.build(player);

			if (kitType.getSpecialItems() != null && kitType.getSpecialItems()[0].getStack().getType().equals(Material.CACTUS))
				player.getInventory().setHelmet(kitType.getSpecialItems()[0].getStack());

		} else {
			new ItemBuilder(Material.WOOD_SWORD).setUnbreakable().setName("§b§lWood Sword").build(player);
		}

		CacheItems.COMPASS.build(player);

		if (kitType.getSpecialItems() != null) {
			for (ItemBuilder items : kitType.getSpecialItems()) {
				if (items.getStack().getType().equals(Material.CACTUS))
					continue;

				String name = items.getStack().getType().name().toLowerCase();
				String itemName = getManager().getUtils().captalize(name.contains("_") ? name.split("_")[1] : name);

				items.setName("§b§l" + itemName).build(player);
			}
		}

		if (!simulator)
			getManager().getGamerManager().fillInventory(player, CacheItems.SOUP, 36);

	}

	public List<KitType> getPlayerKits(Player player) {
		List<KitType> playerKits = new ArrayList<>();
		for (KitType kitType : KitType.values()) {
			if (player.hasPermission("pvp.kit." + kitType.getName().toLowerCase()) || player.hasPermission("pvp.kit.*")) {
				playerKits.add(kitType);
			}
		}
		return playerKits;
	}

	public KitType getFromString(String kitName) {
		for (KitType kitTypes : KitType.values()) {
			if (kitTypes.getName().equalsIgnoreCase(kitName))
				return kitTypes;
		}
		return null;
	}

	public boolean containsKit(String kit) {
		for (KitType kitTypes : KitType.values()) {
			if (kit.equalsIgnoreCase(kitTypes.toString()))
				return true;
		}
		return false;
	}

	public void addCooldown(Player player, double segundos) {
		cooldown.put(player.getUniqueId(), (long) (System.currentTimeMillis() + (segundos * 1000)));
	}

	public void removeCooldown(Player player) {
		cooldown.remove(player.getUniqueId());
	}

	public boolean inCooldown(Player player) {
		return cooldown.containsKey(player.getUniqueId()) ? getCooldown(player) / 100 >= 0 ? true : false : false;
	}

	public double getCooldown(Player player) {
		return cooldown.contains(player.getUniqueId()) ? 0
				: (cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 10;
	}

	public void sendCooldown(Player player) {
		double cooldown = getCooldown(player) / 100;

		player.sendMessage("§e§lCOOLDOWN §fAguarde mais §6" + cooldown + "§f segundos para usar novamente!");
	}
	
}
