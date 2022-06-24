package br.com.zenix.pvp.commands.player.warp;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.warps.type.WarpType;

public class OneVsOneCommand extends PvPCommand {
	
	public OneVsOneCommand() {
		super("1v1");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender))
			return true;

		Player player = (Player) sender;
		
		if(getManager().getGamerManager().getGamer(player).inCombat()){
			return true;
		}
		
		FightMode fightMode = getManager().getBattleManager().getFight();
		
		if (fightMode.isFighting(player.getUniqueId())){
			return true;
		}
		
		getManager().getWarpTeleport().teleportPlayer(player, WarpType.ONE_VS_ONE);
		
		return true;
	}
	
}
