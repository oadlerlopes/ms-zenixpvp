package br.com.zenix.pvp.kits.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class Anchor extends PvPListener {

	@EventHandler
	public void onAnchor(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;

		if (getManager().getGamerManager().getGamer(event.getEntity().getUniqueId()).getKit().equals(KitType.ANCHOR)
				|| getManager().getGamerManager().getGamer(event.getDamager().getUniqueId()).getKit()
						.equals(KitType.ANCHOR)) {
			anchor((Player) event.getEntity());
			anchor((Player) event.getDamager());
		}
	}

	private void anchor(final Player player) {
		player.setVelocity(new Vector(0, 0, 0));

		new BukkitRunnable() {
			public void run() {
				player.setVelocity(new Vector(0, 0, 0));
			}
		}.runTaskLater(getManager().getPlugin(), 1L);
	}

}
