package br.com.zenix.pvp.player.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.utilitaries.item.constructor.ItemBuilder;

public class KitInventory extends PvPListener {

	private ItemBuilder ib;
	private static String kitPrefix = "§b§l";

	private Player player;
	@SuppressWarnings("unused")
	private Gamer gamer;

	private int id;
	private String title;
	private Inventory inventory;

	private KitInventoryMode mode;

	public KitInventory() {
	}

	public KitInventory(Player player, KitInventoryMode mode, Integer index) {
		this.player = player;
		this.mode = mode;

		ib = new ItemBuilder(Material.AIR);
		gamer = getManager().getGamerManager().getGamer(player.getUniqueId());
		id = index;

		generate();
	}

	public enum KitInventoryMode {
		YOUR_KITS, SIMULATOR_KITS;
	}

	private boolean hasKit(String kitName) {
		return player.hasPermission("pvp.kit." + kitName) || player.hasPermission("pvp.kit.*");
	}

	private List<ItemStack> getKits(KitInventoryMode mode, Player player) {

		ArrayList<KitType> kitTypes = new ArrayList<>();
		List<ItemStack> toShow = new ArrayList<>();

		for (KitType kitType : KitType.values()) {
			kitTypes.add(kitType);
		}

		for (int range : range(1 * 7 - 6, (1 + 3) * 7 - 6)) {
			KitType kitType = null;

			if (range < kitTypes.size())
				kitType = kitTypes.get(range);

			if (toShow.size() > 44)
				break;

			if (kitType == null) {
	
			} else {

				if (mode.equals(KitInventoryMode.YOUR_KITS) && hasKit(kitType.getName())) {
					if (kitType.equals(KitType.NONE))
						continue;

					toShow.add(ib.setMaterial(kitType.getIcon()).setName(kitPrefix + kitType.getName())
							.setDescription(getDescription(kitType)).setBreakable().getStack());
				} else if (mode.equals(KitInventoryMode.SIMULATOR_KITS)) {
					if (player.hasPermission("pvp.kit." + kitType))
						toShow.add(ib.setMaterial(kitType.getIcon()).setName(kitPrefix + kitType.getName().toUpperCase())
								.setDescription(getDescription(kitType)).setBreakable().getStack());
				}
			}

		}

		return toShow;

	}

	private String getDescription(KitType kitType) {
		String description = new String();

		if (mode.equals(KitInventoryMode.YOUR_KITS) || mode.equals(KitInventoryMode.SIMULATOR_KITS)) {
			description = "\n§f" + kitType.getDescription() + "";
		}

		return description;
	}

	public void update(Player player, Inventory inventory) {

		inventory.clear();

		int slot = 11;
		List<ItemStack> items = getKits(mode, player);
		for (ItemStack item : items) {

			if (slot % 9 > 7)
				slot += 3;

			inventory.setItem(slot, item);

			slot++;

		}

		new ItemBuilder(Material.CHEST).setName("§7Seus Kits").setGlowed(false).build(inventory, 9);

		player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		player.updateInventory();
	}

	private void generate() {

		int slots = 9;

		if (mode.equals(KitInventoryMode.YOUR_KITS)) {
			title = "Seus kits (" + id + ")";
			slots = 54;
		} else if (mode.equals(KitInventoryMode.SIMULATOR_KITS)) {
			title = "Simulador";
			slots = 9;
		}

		inventory = Bukkit.createInventory(player, slots, title);
		new ItemBuilder(Material.CHEST).setName("§7Seus Kits").setGlowed(false).build(inventory, 9);

		player.playSound(player.getLocation(), Sound.CLICK, 5F, 5F);
		player.updateInventory();

		player.openInventory(inventory);

		update(player, inventory);
	}

	@EventHandler
	private void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() != null) {
			if (event.getClickedInventory().getTitle().startsWith("Seus kits (")) {
				event.setCancelled(true);
				if (event.getCurrentItem() != null) {
					Material type = event.getCurrentItem().getType();
					Integer index = Integer.valueOf(
							event.getClickedInventory().getTitle().replace("Seus kits (", "").replace(")", ""));

					if (type == Material.AIR)
						return;

					if (!type.equals(Material.WOOL) && !type.equals(Material.STAINED_GLASS_PANE)
							&& !type.equals(Material.CHEST) && !type.equals(Material.AIR)) {
						KitType kitType = getManager().getKitManager().getFromString(
								event.getCurrentItem().getItemMeta().getDisplayName().replace(kitPrefix, ""));
						getManager().getGamerManager().getGamer((Player) event.getWhoClicked()).setKit(kitType);
						getManager().getKitManager().giveKit((Player) event.getWhoClicked(), kitType, false);
					} else if (type.equals(Material.GOLD_INGOT)) {

					} else if (type.equals(Material.INK_SACK)) {
						if (event.getCurrentItem().getDurability() == 10) {
							index++;
							new KitInventory((Player) event.getWhoClicked(), KitInventoryMode.YOUR_KITS, index);
						} else if (event.getCurrentItem().getDurability() == 1) {
							index--;
							new KitInventory((Player) event.getWhoClicked(), KitInventoryMode.YOUR_KITS, index);
						}
					}
				}
			}
		}
	}

	public int[] range(int start, int stop) {
		int[] result = new int[stop - start];

		for (int i = 0; i < stop - start; i++)
			result[i] = start + i;

		return result;
	}

}
