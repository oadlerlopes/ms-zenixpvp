package br.com.zenix.pvp.warps.constructor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.gamer.GamerManager;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.utilitaries.Variables;
import br.com.zenix.pvp.utilitaries.armor.ArmorUtilitaries;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;
import br.com.zenix.pvp.warps.type.WarpType;

public class ItemConstructor {

	public ItemConstructor(Manager manager, Player player, WarpType warp) {
		PlayerInventory inventory = player.getInventory();
		ItemBuilder item = new ItemBuilder(Material.AIR);
		GamerManager gamerManager = PvP.getManager().getGamerManager();
		Gamer gamer = gamerManager.getGamer(player);

		item = CacheItems.DEFAULT_SWORD.getItem(0).setMaterial(Material.DIAMOND_SWORD).setUnbreakable();

		item.setUnbreakable();

		/* Preparing the player */

		player.closeInventory();
		inventory.clear();
		inventory.setArmorContents(null);
		player.setFireTicks(0);

		/* Adding items and armors */

		if (warp.equals(WarpType.FPS)) {
			if (Variables.FULL_IRON) {
				item.setEnchant(Enchantment.DAMAGE_ALL, 1).build(inventory, 0);
				ArmorUtilitaries.updateArmor(player, "IRON", true);
			} else {
				item.setType(Material.WOOD_SWORD).setName("§b§lWood Sword").setUnbreakable()
						.setEnchant(Enchantment.DAMAGE_ALL, 1).build(inventory, 0);
			}

			manager.getGamerManager().fillInventory(player, CacheItems.SOUP, 36);

			gamer.setPvP(false);

		} else if (warp.equals(WarpType.LAVA)) {
			CacheItems.RECRAFT.build(inventory);
			manager.getGamerManager().fillInventory(player, CacheItems.SOUP, 36);
			gamer.setPvP(false);
		} else if (warp.equals(WarpType.ONE_VS_ONE)) {
			item.setMaterial(Material.BLAZE_ROD).setName("§aDesafiar para 1v1§7 (Normal)").build(inventory, 3);
			item.setMaterial(Material.INK_SACK).setName("§a1v1 Rápido§7 (Clique)").setDurability(8).build(inventory, 5);
			gamer.setPvP(false);

		}

	}

}
