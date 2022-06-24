package br.com.zenix.pvp.commands.player.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.warps.type.WarpType;

public class KitCommand extends PvPCommand {

	public KitCommand() {
		super("kit");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;
		Gamer gamer = getManager().getGamerManager().getGamer(player.getUniqueId());

		if (args.length == 0) {
			sendArgumentMessage(sender, "kit", "/kit (kit)");
			return true;
		}

		KitType kitType = getManager().getKitManager().getFromString(args[0]);

		if (args.length == 1) {

			if (kitType == null || kitType.equals(KitType.NONE)) {
				player.sendMessage("§6§lKIT §fO kit §6§l" + args[0] + "§f não foi encontrado!");
				return true;
			} else if (!gamer.getWarp().equals(WarpType.NONE)) {
				player.sendMessage("§6§lKIT §fVocê não pode pegar kits agora!");
				return true;
			} else if (!gamer.getKit().equals(KitType.NONE)) {
				player.sendMessage("§6§lKIT §fVocê já está utilizando um kit!");
				return true;
			} else if (!player.hasPermission("pvp.kit." + kitType.getName())) {
				player.sendMessage("§6§lKIT §fVocê não possui o kit §6§l" + args[0].toUpperCase() + "§f!");
				return true;
			}

			getManager().getKitManager().giveKit(player, kitType, false);
		}
		return true;
	}

}
