package br.com.zenix.pvp.managers.managements;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.zenix.core.spigot.player.events.GamerHitEntityEvent;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.warps.type.WarpType;

public class DamageManager extends PvPListener {

	public static final HashMap<Material, Double> damageMaterial = new HashMap<>();

	public DamageManager() {

		damageMaterial.put(Material.DIAMOND_SWORD, 4.5D);
		damageMaterial.put(Material.IRON_SWORD, 4.0D);
		damageMaterial.put(Material.STONE_SWORD, 3.5D);
		damageMaterial.put(Material.WOOD_SWORD, 2.0D);
		damageMaterial.put(Material.GOLD_SWORD, 2.0D);

		damageMaterial.put(Material.DIAMOND_AXE, 5.0D);
		damageMaterial.put(Material.IRON_AXE, 4.0D);
		damageMaterial.put(Material.STONE_AXE, 3.0D);
		damageMaterial.put(Material.WOOD_AXE, 2.0D);
		damageMaterial.put(Material.GOLD_AXE, 2.0D);

		damageMaterial.put(Material.DIAMOND_PICKAXE, 4.0D);
		damageMaterial.put(Material.IRON_PICKAXE, 3.0D);
		damageMaterial.put(Material.STONE_PICKAXE, 2.0D);
		damageMaterial.put(Material.WOOD_PICKAXE, 1.0D);
		damageMaterial.put(Material.GOLD_PICKAXE, 1.0D);

		for (Material mat : Material.values()) {
			if (damageMaterial.containsKey(mat)) {
				continue;
			}
			damageMaterial.put(mat, 1.0D);
		}
	}

	@EventHandler
	public void onLava(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && e.getCause() == DamageCause.LAVA) {
			e.setDamage(4.0D);
		}
	}

	@EventHandler
	public void onAsyncPreDamageEvent(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getDamager();

		if (player.hasMetadata("custom")) {
			player.removeMetadata("custom", getManager().getPlugin());
			return;
		}

		double damage = 1.0D;

		ItemStack itemStack = player.getItemInHand();

		if (itemStack != null) {
			damage = damageMaterial.get(itemStack.getType());
			if (itemStack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
				damage += itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			}
		}

		for (PotionEffect effect : player.getActivePotionEffects()) {
			if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
				int amplifier = effect.getAmplifier() + 1;
				damage += (amplifier * 2);
			} else if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
				damage -= (effect.getAmplifier() + 1);
			}
		}

		if (event.getEntity() instanceof LivingEntity) {
			GamerHitEntityEvent gamerEvent = new GamerHitEntityEvent(player, (LivingEntity) event.getEntity(), damage);
			Bukkit.getPluginManager().callEvent(gamerEvent);
			if (gamerEvent.isCancelled()) {
				event.setCancelled(true);
				return;
			} else {
				LivingEntity le = (LivingEntity) event.getEntity();
				if (le.hasPotionEffect(PotionEffectType.WEAKNESS)) {
					for (PotionEffect effect : le.getActivePotionEffects()) {
						if (!effect.getType().equals(PotionEffectType.WEAKNESS)) {
							continue;
						}
						gamerEvent.setDamage(gamerEvent.getDamage() + (effect.getAmplifier() + 1));
					}
				}
				if (player.hasPermission("*")) {
					damage = gamerEvent.getDamage() + 1;
				} else {
					damage = gamerEvent.getDamage();
				}

			}
		}
		event.setDamage(damage);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHitEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getDamager();

		if (player.hasMetadata("custom")) {
			player.removeMetadata("custom", getManager().getPlugin());
			return;
		}

		double damage = 1.0D;

		ItemStack itemStack = player.getItemInHand();

		if (itemStack != null) {
			damage = damageMaterial.get(itemStack.getType());
			if (itemStack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
				damage += itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			}
		}

		for (PotionEffect effect : player.getActivePotionEffects()) {
			if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
				int amplifier = effect.getAmplifier() + 1;
				damage += (amplifier * 2);
			} else if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
				damage -= (effect.getAmplifier() + 1);
			}
		}

		if (event.getEntity() instanceof LivingEntity) {
			GamerHitEntityEvent gamerEvent = new GamerHitEntityEvent(player, (LivingEntity) event.getEntity(), damage);
			Bukkit.getPluginManager().callEvent(gamerEvent);
			if (gamerEvent.isCancelled()) {
				event.setCancelled(true);
				return;
			} else {
				LivingEntity le = (LivingEntity) event.getEntity();
				if (le.hasPotionEffect(PotionEffectType.WEAKNESS)) {
					for (PotionEffect effect : le.getActivePotionEffects()) {
						if (!effect.getType().equals(PotionEffectType.WEAKNESS)) {
							continue;
						}
						gamerEvent.setDamage(gamerEvent.getDamage() + (effect.getAmplifier() + 1));
					}
				}
				if (player.hasPermission("*")) {
					damage = gamerEvent.getDamage() + 1;
				} else {
					damage = gamerEvent.getDamage();
				}

			}
		}
		event.setDamage(damage);
	}
	

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();
			if (arrow.getShooter() instanceof Player) {
				arrow.getShooter();
				arrow.getWorld().playSound(arrow.getLocation(), Sound.ORB_PICKUP, 10.0F, 5.0F);
				arrow.remove();
			}
		}

	}

	@EventHandler
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Gamer dataEntity = getManager().getGamerManager().getGamer(event.getEntity().getUniqueId());
			Gamer dataDamager = getManager().getGamerManager().getGamer(event.getDamager().getUniqueId());

			if (((Player) event.getEntity()).getLocation().getWorld().equals(Bukkit.getWorld("lava"))
					|| ((Player) event.getDamager()).getLocation().getWorld().equals(Bukkit.getWorld("lava"))) {
				event.setCancelled(true);
				return;
			}

			if (dataEntity.getWarp().equals(WarpType.LAVA) || dataDamager.getWarp().equals(WarpType.LAVA)) {
				event.setCancelled(true);
				return;
			}

			if (!dataEntity.hasPvP() || !dataDamager.hasPvP()) {
				event.setCancelled(true);
				return;
			}

			if (!dataEntity.inCombat()) {
				dataEntity.setCombat(10);
			}

			if (!dataDamager.inCombat())
				dataDamager.setCombat(10);

		} else if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

			if (gamer.getWarp().equals(WarpType.LAVA)) {
				event.setCancelled(true);
				return;
			}

			if (!gamer.hasPvP()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

			if (!gamer.hasPvP() && !gamer.getWarp().equals(WarpType.LAVA)) {
				event.setCancelled(true);
			}
		}
	}

}
