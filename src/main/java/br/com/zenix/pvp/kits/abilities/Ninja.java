package br.com.zenix.pvp.kits.abilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import br.com.zenix.core.spigot.player.particle.ParticleType;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Guilherme Fane, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */

public class Ninja extends PvPListener {

	private static HashMap<UUID, Player> target = new HashMap<>();

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!event.isCancelled() && event.getEntity() instanceof Player && event.getDamager() instanceof Player
				&& getManager().getGamerManager().getGamer(event.getDamager().getUniqueId()).getKit()
						.equals(KitType.NINJA)) {
			target.put(event.getDamager().getUniqueId(), ((Player) event.getEntity()));
		}
	}

	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		if (event.isSneaking() && target.containsKey(event.getPlayer().getUniqueId())) {

			if (getManager().getKitManager().inCooldown(event.getPlayer())) {
				getManager().getKitManager().sendCooldown(event.getPlayer());
				return;
			}

			Player targetPlayer = target.get(event.getPlayer().getUniqueId());

			if (event.getPlayer().getLocation().getY() > 140) {
				return;
			}

			if (targetPlayer == null || !targetPlayer.isOnline()) {
				event.getPlayer().sendMessage("§c§lNINJA §fO player não está §4§lONLINE");
			} else if (targetPlayer.getLocation().distance(event.getPlayer().getLocation()) > 100.0D) {
				event.getPlayer().sendMessage("§c§lNINJA §fO player está muito §4§lLONGE§f de você!");
			} else {
				spawnNinjaEffects(event.getPlayer().getLocation());
				event.getPlayer().teleport(targetPlayer.getLocation());
				target.remove(event.getPlayer().getUniqueId());
				getManager().getKitManager().addCooldown(event.getPlayer(), 5);
			}

		}
	}

	private void spawnNinjaEffects(Location loc) {
		ParticleType.LARGE_SMOKE.setParticle(loc, 0, 0, 0, 0.05F, 300);
	}
}
