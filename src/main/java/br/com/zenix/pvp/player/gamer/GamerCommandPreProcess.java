package br.com.zenix.pvp.player.gamer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class GamerCommandPreProcess extends PvPListener {

	@EventHandler
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();

		if (message.equals("/kill")) {
			event.setCancelled(true);
			return;
		}

		if (message.startsWith("/") && getManager().getGamerManager().getGamer(event.getPlayer()).inCombat()
				&& !event.getPlayer().hasPermission("pvp.bypass.combat")) {
			event.setCancelled(true);
			player.sendMessage("§cVocê está em combate!");
			return;
		}

		if (getManager().getGamerManager().getGamer(player).getWarp().equals(WarpType.ONE_VS_ONE)
				&& !message.startsWith("/spawn") && !message.startsWith("/fake ") && !message.equalsIgnoreCase("/fake")
				&& !message.equalsIgnoreCase("/tag") && !message.equalsIgnoreCase("/tell")
				&& !message.equalsIgnoreCase("/ban") && !message.startsWith("/tag") && !message.startsWith("/ban")
				&& !message.startsWith("/mute") && !message.startsWith("/tempban") && !message.startsWith("/tempmute")
				&& !message.startsWith("/rank") && !message.startsWith("/r") && !message.startsWith("/tell")
				&& !message.startsWith("/ping") && !message.startsWith("/liga") && !message.startsWith("/admin")
				&& !message.startsWith("/1v1") && !player.hasPermission("op")) {
			event.setCancelled(true);

			if (!getManager().getOneVsOneManager().getFight().isFighting(player.getUniqueId())) {
				player.sendMessage("§cVocê não pode usar esse comando agora!");
			} else {
				player.sendMessage("§cVocê não pode usar esse comando agora!");
			}

			return;
		}
	}
}
