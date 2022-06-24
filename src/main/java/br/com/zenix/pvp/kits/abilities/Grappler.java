package br.com.zenix.pvp.kits.abilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.utilitaries.kits.HookUtil;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class Grappler extends PvPListener {
	
	private HashMap<UUID, HookUtil> hooks = new HashMap<UUID, HookUtil>();

	@EventHandler
	private void onGrappler(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (gamer.getKit().equals(KitType.GRAPPLER) && player.getItemInHand().getType().equals(Material.LEASH)) {
			event.setCancelled(true);

			Location location1 = player.getLocation();

			if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {

				if (hooks.containsKey(player.getUniqueId()))
					hooks.get(player.getUniqueId()).remove();

				Vector direction = location1.getDirection();
				HookUtil nms = new HookUtil(player.getWorld(), ((CraftPlayer) player).getHandle());

				nms.spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()));
				nms.move(5 * direction.getX(), 5 * direction.getY(), 5 * direction.getZ());

				hooks.put(player.getUniqueId(), nms);

			} else if (hooks.containsKey(player.getUniqueId()) && hooks.get(player.getUniqueId()).isHooked()) {

				Location location2 = hooks.get(player.getUniqueId()).getBukkitEntity().getLocation();

				double distance = location2.distance(location1);
				double vectorX = (1 + 0.07 * distance) * (location2.getX() - location1.getX()) / distance;
				double vectorY = (1 + 0.03 * distance) * (location2.getY() - location1.getY()) / distance;
				double vectorZ = (1 + 0.07 * distance) * (location2.getZ() - location1.getZ()) / distance;

				player.setVelocity(new Vector(vectorX, vectorY, vectorZ));
				player.setFallDistance(0.0F);
			}
		}
	}

	/* Removing the hook on item held */

	@EventHandler
	private void onRemoveLeash(PlayerItemHeldEvent event) {
		if (hooks.containsKey(event.getPlayer().getUniqueId())) {
			hooks.get(event.getPlayer().getUniqueId()).remove();
			hooks.remove(event.getPlayer().getUniqueId());
		}
	}
}
