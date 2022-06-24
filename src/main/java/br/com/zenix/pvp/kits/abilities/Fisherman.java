package br.com.zenix.pvp.kits.abilities;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class Fisherman extends PvPListener {

	@EventHandler
	public void onFisherman(PlayerFishEvent e) {
		Player p = e.getPlayer();
		
		if (getManager().getGamerManager().getGamer(p).getKit() == KitType.FISHERMAN) {
			e.getPlayer().getItemInHand().setDurability((short) 0);

			if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
				Entity c = e.getCaught();
				Player sujeito = ((Player) c).getPlayer();

				if (sujeito == p) {
					p.sendMessage("§cSinceramente meu jovem, não entendi o motivo de tentar se pescar.");
					return;
				}

				World w = p.getLocation().getWorld();
				double x = p.getLocation().getBlockX() + 0.5D;
				double y = p.getLocation().getBlockY();
				double z = p.getLocation().getBlockZ() + 0.5D;
				float yaw = c.getLocation().getYaw();
				float pitch = c.getLocation().getPitch();
				Location loc = new Location(w, x, y, z, yaw, pitch);
				c.teleport(loc);
			}
		}
	}

}
