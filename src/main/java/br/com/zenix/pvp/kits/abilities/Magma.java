package br.com.zenix.pvp.kits.abilities;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import br.com.zenix.core.spigot.player.events.ServerTimeEvent;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class Magma extends PvPListener {

	@EventHandler
	public void onMagma(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player p = (Player) entity;
		if (!(getManager().getGamerManager().getGamer(p).getKit() == KitType.MAGMA)) {
			return;
		}
		EntityDamageEvent.DamageCause fire = event.getCause();
		if ((fire == EntityDamageEvent.DamageCause.FIRE) || (fire == EntityDamageEvent.DamageCause.LAVA)
				|| (fire == EntityDamageEvent.DamageCause.FIRE_TICK)
				|| (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDAmager(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Player d = (Player) e.getDamager();
		if (!(getManager().getGamerManager().getGamer(p).getKit() == KitType.MAGMA)) {
			return;
		}
		if (getManager().getGamerManager().getGamer(d).getKit() == KitType.NONE) {
			return;
		}
		Random r = new Random();
		if (((d instanceof Player)) && (r.nextInt(5) == 0)) {
			d.setFireTicks(100);
		}
	}

	@EventHandler
	public void onPoseidon(ServerTimeEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getManager().getGamerManager().getGamer(p).getKit() == KitType.MAGMA) {
				Block b = p.getLocation().getBlock();
				if (b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER) {
					p.damage(2.0);
				}
			}
		}
	}

}
