package br.com.zenix.pvp.commands.player.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.warps.type.WarpType;

public class LavaCommand extends PvPCommand {
	
	public LavaCommand() {
		super("lava", "challenge");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;
		
		if(getManager().getGamerManager().getGamer(player).inCombat()){
			return true;
		}
		
		getManager().getConfigManager().teleportPlayer(player, WarpType.LAVA);
		getManager().getWarpTeleport().teleportPlayer(player, WarpType.LAVA);
		
		return true;
	}
	
}
