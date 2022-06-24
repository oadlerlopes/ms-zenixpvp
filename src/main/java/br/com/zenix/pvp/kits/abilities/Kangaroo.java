package br.com.zenix.pvp.kits.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class Kangaroo extends PvPListener {

	private static HashMap<UUID, Long> hitNerf = new HashMap<>();
	private static List<UUID> kangarooUses = new ArrayList<>();

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (getManager().getGamerManager().getGamer(player).getKit().equals(KitType.KANGAROO)
				&& event.getAction() != Action.PHYSICAL && event.getItem() != null
				&& event.getItem().getType().equals(Material.FIREWORK)) {
			event.setCancelled(true);

			if (kangarooUses.contains(player.getUniqueId()))
				return;

			if (hitNerf.containsKey(player.getUniqueId())
					&& hitNerf.get(player.getUniqueId()) > System.currentTimeMillis()) {
				player.setVelocity(new Vector(0, -1.0, 0));
				player.sendMessage("§c§lKANGAROO §fVocê está em §4§lCOMBATE§f, aguarde para usar sua habilidade.");
				return;
			}

			Vector vector = player.getEyeLocation().getDirection();
			if (player.isSneaking()) {
				vector = vector.multiply(1.8F).setY(0.5F);
			} else {
				vector = vector.multiply(0.5F).setY(1F);
			}

			player.setFallDistance(-1.0F);
			player.setVelocity(vector);
			kangarooUses.add(player.getUniqueId());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (kangarooUses.contains(e.getPlayer().getUniqueId())
				&& (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR
						|| e.getPlayer().isOnGround())) {
			kangarooUses.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!getManager().getGamerManager().getGamer(((Player) event.getEntity())).getKit().equals(KitType.KANGAROO))
			return;
		if (event.isCancelled())
			return;
		if (!(event.getDamager() instanceof LivingEntity))
			return;
		if (event.getDamager() instanceof Player
				&& getManager().getGamerManager().getGamer((Player) event.getDamager()).hasPvP())
			return;

		hitNerf.put(event.getEntity().getUniqueId(), System.currentTimeMillis() + 5000L);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player
				&& getManager().getGamerManager().getGamer(((Player) e.getEntity())).getKit().equals(KitType.KANGAROO)
				&& e.getCause() == DamageCause.FALL && e.getDamage() > 7.0D) {
			e.setDamage(7.0D);
		}
	}
}
