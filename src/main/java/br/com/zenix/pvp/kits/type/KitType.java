package br.com.zenix.pvp.kits.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public enum KitType {

	NONE("Nenhum", Material.STONE_SWORD, null, false, "Sem nenhuma habilidade definida"),
	PVP("PvP", Material.DIAMOND_SWORD, null, true, "Não possui habilidade alguma"),
	NINJA("Ninja", Material.NETHER_STAR, null, true, "Aperte shift e se teletransporte"),
	ANCHOR("Anchor", Material.ANVIL, null, false, "Tenha uma ancora no chão e leve muito pouco knockback de players."),
	MAGMA("Magma", Material.MAGMA_CREAM, null, false, "Tenha 30% de chance de queimar seu oponente."),
	SNAIL("Snail", Material.SOUL_SAND, null, false, "Tenha 30% de chance de aplicar lentidao em seu oponente."),
	KANGAROO("Kangaroo", Material.FIREWORK, new ItemBuilder[] { new ItemBuilder(Material.FIREWORK) }, false, "Transforme-se em um canguru e dê grandes pulos para correr atrás dos seus inimigos."),
	GRAPPLER("Grappler", Material.LEASH, new ItemBuilder[] { new ItemBuilder(Material.LEASH) }, false, "Tenha suas hablidades de um escalador profissional e com sua corda, ande rapidamente."),
	FISHERMAN("Fisherman", Material.FISHING_ROD, new ItemBuilder[] { new ItemBuilder(Material.FISHING_ROD).setUnbreakable() }, false, "Pesque seus inimigos e mate-os!"),
	ARCHER("Archer", Material.BOW, new ItemBuilder[] { new ItemBuilder(Material.BOW).setEnchant(Enchantment.ARROW_INFINITE, 1), new ItemBuilder(Material.ARROW) }, false, "Use seu arco com Infinidade para acertar os seus inimigos."),
	MONK("Monk", Material.BLAZE_ROD, new ItemBuilder[] { new ItemBuilder(Material.BLAZE_ROD) }, false, "Tire as armas das mãos dos inimigos!");

	private String name, description;
	private Integer price;
	private ItemBuilder[] specialItems;

	private Material icon;
	private boolean defaultSword;

	private KitType(String name, Material icon, ItemBuilder[] specialItems, boolean defaultSword, String description) {
		this.name = name;
		this.icon = icon;
		this.specialItems = specialItems;
		this.defaultSword = defaultSword;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getPrice() {
		return price;
	}

	public Material getIcon() {
		return icon;
	}

	public ItemBuilder[] getSpecialItems() {
		return specialItems;
	}

	public boolean withDefaultSword() {
		return defaultSword;
	}

	public List<KitType> getPlayerKits(Player player) {
		List<KitType> playerKits = new ArrayList<>();
		for (KitType kitType : KitType.values()) {
			if (player.hasPermission("pvp.kit." + kitType.getName().toLowerCase())
					|| player.hasPermission("pvp.kit.*")) {
				playerKits.add(kitType);
			}
		}
		return playerKits;
	}

	public static KitType getKit(String string) {
		KitType kitType = null;
		for (KitType kitTypes : values())
			if (kitTypes.getName().equalsIgnoreCase(string))
				kitType = kitTypes;
		return kitType;
	}

}
