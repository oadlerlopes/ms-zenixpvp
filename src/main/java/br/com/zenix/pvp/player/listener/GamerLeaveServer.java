package br.com.zenix.pvp.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class GamerLeaveServer extends PvPListener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);
		gamer.update();

		getManager().getAdminManager().setPlayerQuit(player);
	}

}
