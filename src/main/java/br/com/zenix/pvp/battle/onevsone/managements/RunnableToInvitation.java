package br.com.zenix.pvp.battle.onevsone.managements;

import org.bukkit.entity.Player;

import br.com.zenix.pvp.battle.onevsone.modules.BattleModules;

public class RunnableToInvitation {

	private Player to;
	private BattleModules battleModules;
	
	public RunnableToInvitation(Player to, BattleModules battleModules) {
		this.to = to;
		this.battleModules = battleModules;
	}
	
	public Player gePlayerTo() {
		return to;
	}
	
	public BattleModules getBattleType() {
		return battleModules;
	}
	
}
