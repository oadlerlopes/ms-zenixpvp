package br.com.zenix.pvp.player.gamer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;

/**
 * Copyright (C) Adler Lopes, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class GamerAccountLoad extends PvPListener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void login(PlayerLoginEvent event) {
		if (event.getResult() != org.bukkit.event.player.PlayerLoginEvent.Result.ALLOWED)
			return;

		Player player = event.getPlayer();
		Gamer gamer = new Gamer(getManager().getCoreManager().getAccountManager().getAccount(player));
		getManager().getGamerManager().addGamer(gamer);

		getManager().getGamerManager().getLogger().log(
				"The player with uuid " + player.getUniqueId() + "(" + player.getName() + ") was loaded correctly.");
	}


}
