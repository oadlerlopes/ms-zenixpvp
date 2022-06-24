package br.com.zenix.pvp.warps.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.warps.constructor.ItemConstructor;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this
 * file, via any medium is strictly prohibited proprietary and confidential
 */
public class PlayerCompleteChallenge extends PvPListener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (gamer == null || player == null)
			return;

		if (gamer.getWarp().equals(WarpType.LAVA)) {

			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GLOWSTONE
					|| player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GLOWSTONE
					|| player.getLocation().getBlock().getType() == Material.GLOWSTONE
					|| player.getLocation().getBlock().getType() == Material.GLOWSTONE) {
				
				player.sendMessage("§eVocê conseguiu concluir a lava-challenge! §cParabéns!");

				getManager().getGamerManager().resetPlayer(player);
				new ItemConstructor(getManager(), player, WarpType.LAVA);
				getManager().getConfigManager().teleportPlayer(player, WarpType.LAVA);
				gamer.setPvP(false);
			} else {
				gamer.setPvP(false);
			}

			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK
					|| player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK
					|| player.getLocation().getBlock().getType() == Material.BEDROCK
					|| player.getLocation().getBlock().getType() == Material.BEDROCK) {

				player.sendMessage("§eVocê conseguiu concluir a lava-challenge! §cParabéns!");
				
				Bukkit.broadcastMessage(
						"§4§lCHALLENGE §fO player §c§l" + player.getName() + "§f concluiu o §4§lCHALLENGE EXTREME!");

				getManager().getGamerManager().resetPlayer(player);
				new ItemConstructor(getManager(), player, WarpType.LAVA);
				getManager().getConfigManager().teleportPlayer(player, WarpType.LAVA);
				gamer.setPvP(false);
			}
		}
	}

}
