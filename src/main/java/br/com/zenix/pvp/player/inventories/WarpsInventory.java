package br.com.zenix.pvp.player.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;
import br.com.zenix.pvp.warps.type.WarpType;

public class WarpsInventory extends PvPListener {

	private String title = "Menu de WarpType";

	public WarpsInventory() {
	}

	public WarpsInventory(Player player) {

		Inventory inventory = Bukkit.createInventory(player, 9, title);

		int i = 2;
		for (ItemBuilder items : CacheItems.WARPS.getItems()) {
			inventory.setItem(i, items.getStack());
			i++;
		}

		player.openInventory(inventory);

	}

	@EventHandler
	private void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null && event.getClickedInventory().getTitle().equals(title)) {
			event.setCancelled(true);

			Material type = event.getCurrentItem().getType();

			if (event.getCurrentItem() != null && !type.equals(Material.THIN_GLASS) && !type.equals(Material.AIR)
					&& !type.equals(Material.STAINED_GLASS_PANE)) {
				// WarpType warp = WarpType
				// .getFromString(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
				if (type.equals(Material.BLAZE_ROD)) {
					getManager().getConfigManager().teleportPlayer((Player) event.getWhoClicked(), WarpType.ONE_VS_ONE);
					PvP.getManager().getWarpTeleport().teleportPlayer((Player) event.getWhoClicked(), WarpType.ONE_VS_ONE);
				} else if (type.equals(Material.GLASS)) {
					getManager().getConfigManager().teleportPlayer((Player) event.getWhoClicked(), WarpType.FPS);
					PvP.getManager().getWarpTeleport().teleportPlayer((Player) event.getWhoClicked(), WarpType.FPS);
				} else if (type.equals(Material.LAVA)) {
					getManager().getConfigManager().teleportPlayer((Player) event.getWhoClicked(), WarpType.LAVA);
					PvP.getManager().getWarpTeleport().teleportPlayer((Player) event.getWhoClicked(), WarpType.LAVA);
				}
			}
		}
	}

}
