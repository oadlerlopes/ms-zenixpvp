package br.com.zenix.pvp.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import br.com.zenix.pvp.commands.base.PvPListener;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class ServerListener extends PvPListener {
	
	@EventHandler
	public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if (event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN)
			event.setCancelled(true);
	}

	@EventHandler
	private void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	private void onLeavesDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	private void onWeatherChange(WeatherChangeEvent event) {
		event.getWorld().setTime(0);
		event.getWorld().setWeatherDuration(Integer.MAX_VALUE);
		event.setCancelled(true);
	}

	@EventHandler
	private void onPlayerBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	private void onBlockIgnite(BlockIgniteEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDrops(ItemSpawnEvent event) {
		event.getEntity().remove();
	}


}
