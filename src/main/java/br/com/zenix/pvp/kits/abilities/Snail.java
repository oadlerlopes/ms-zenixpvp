package br.com.zenix.pvp.kits.abilities;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.kits.type.KitType;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class Snail extends PvPListener {

	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		Location location = player.getLocation();
		if (!getManager().getGamerManager().getGamer(player).getKit().equals(KitType.SNAIL)) {
			return;
		}
		if (player instanceof Player && new Random().nextInt(3) == 0) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
			location.getWorld().playEffect(location.add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 159, 13);
		}
	}

}
