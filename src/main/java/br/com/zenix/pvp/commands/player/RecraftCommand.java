package br.com.zenix.pvp.commands.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.utilitaries.item.CacheItems;
import br.com.zenix.pvp.warps.type.WarpType;

public class RecraftCommand extends PvPCommand {

	public RecraftCommand() {
		super("recraft");
		command = "§e";
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;
		Gamer gamer = getManager().getGamerManager().getGamer(player);

		if (gamer.getWarp().equals(WarpType.NONE)) {
			if (!gamer.getKit().equals(KitType.NONE)) {
				CacheItems.RECRAFT.build(player);
				player.sendMessage(command + "§fVocê recebeu §6§lRECRAFT§f!");

			} else {
				player.sendMessage(command + "§fVocê precisa §6§lPEGAR§f um kit para poder pegar recraft!");
			}
		} else {
			player.sendMessage(command + "§fVocê está em uma §6§lWARP§f, portanto não pode pegar recraft!");
		}

		return true;
	}

}
