package br.com.zenix.pvp.player.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import br.com.zenix.pvp.commands.base.PvPListener;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class GamerBlockChange extends PvPListener {

	@EventHandler
	private void onBlockBreak(BlockBreakEvent event) {
		if (!event.getPlayer().hasPermission("pvp.build")
				|| !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission("pvp.build")
				|| !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

}
