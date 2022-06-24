package br.com.zenix.pvp.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class GamerMoveChange extends PvPListener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);
		WarpType warp = gamer.getWarp();
		if (!getManager().getAdminManager().isAdmin(player)) {

			if (player.getLocation().getWorld() == null)
				return;

			if (gamer.getKit() == null)
				return;

			if (warp.equals(WarpType.SPAWN) || warp.equals(WarpType.NONE) && !gamer.hasPvP()) {
				if (player.getLocation()
						.getBlockX() > new Location(Bukkit.getWorld("world"), 10051.49, 53, 926.67).getBlockX() + 15
						|| player.getLocation()
								.getBlockX() < -(15
										- new Location(Bukkit.getWorld("world"), 10051.49, 53, 926.67).getBlockX())
						|| player.getLocation()
								.getBlockZ() > new Location(Bukkit.getWorld("world"), 10051.49, 53, 926.67).getBlockZ()
										+ 15
						|| player.getLocation().getBlockZ() < -(15
								- new Location(Bukkit.getWorld("world"), 10051.49, 53, 926.67).getBlockZ())) {

					if (warp.equals(WarpType.SPAWN) || warp.equals(WarpType.NONE) && !gamer.hasPvP()) {
						if (gamer.getKit().equals(KitType.NONE)) {
							gamer.setKit(KitType.PVP);
							getManager().getKitManager().giveKit(player, KitType.PVP, false);
						}
						gamer.setPvP(true);
						player.sendMessage("§cVocê perdeu a proteção do Spawn!");
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 100000));
					}
				}
			}
			if (warp.equals(WarpType.FPS)) {
				if (player.getLocation().getBlockX() > 0 + 5 || player.getLocation().getBlockX() < -(5 - 0)
						|| player.getLocation().getBlockZ() > 0 + 5 || player.getLocation().getBlockZ() < -(5 - 0)) {

					if (!gamer.hasPvP()) {
						gamer.setPvP(true);
						player.sendMessage("§cVocê perdeu a proteção do Spawn!");
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 100000));
					}
				}
			}
		}
	}

	@EventHandler
	private void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Material blockType = player.getLocation().subtract(0, 1, 0).getBlock().getType();

		if (blockType.equals(Material.GOLD_BLOCK)) {
			player.setVelocity(player.getEyeLocation().getDirection().multiply(5.8F).setY(1.0F));
			player.setFallDistance(-70.0F);
			player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 10);
			player.playSound(player.getLocation(), Sound.ENDERMAN_HIT, 4.0F, 4.0F);
		} else if (blockType.equals(Material.ENDER_PORTAL)) {
			player.setVelocity(new Vector(0, 5, 0));
			player.setFallDistance(-50.0F);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 4.0F, 4.0F);
		} else if (blockType.equals(Material.ENDER_PORTAL_FRAME)) {
			player.setVelocity(new Vector(0, 5, 0));
			player.setFallDistance(-50.0F);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 4.0F, 4.0F);
		} else if (blockType.equals(Material.ENDER_STONE)) {
			player.setVelocity(new Vector(0, 5, 0));
			player.setFallDistance(-50.0F);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 4.0F, 4.0F);
		} else if (blockType.equals(Material.DISPENSER)) {
			player.setVelocity(player.getEyeLocation().getDirection().multiply(4.5F).setY(0.5F));
			player.setFallDistance(-50.0F);
			getManager().getGamerManager().getGamer(player).setPvP(true);
		} else if (blockType.equals(Material.SPONGE)) {
			player.setVelocity(new Vector(0, 5, 0));
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
			player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 4.0F, 4.0F);

			player.setFallDistance(-65.0F);
		}

	}

}
