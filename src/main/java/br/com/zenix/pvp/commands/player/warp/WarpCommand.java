package br.com.zenix.pvp.commands.player.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.warps.type.WarpType;

public class WarpCommand extends PvPCommand {

	public WarpCommand() {
		super("warp");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;

		if (args.length == 0) {
			sendArgumentMessage(sender, "WARP", "/warp´<warp>");
			return true;
		}

		WarpType warp = WarpType.getFromString(args[0]);

		if (args.length == 1) {
			if (warp == null || warp.equals(WarpType.NONE)) {
				player.sendMessage("§d§lWARP §fA warp §d§l" + args[0].toUpperCase() + "§f não foi encontrada!");
				return true;
			}
			getManager().getWarpTeleport().teleportPlayer(player, warp);
		}

		return true;
	}

}
