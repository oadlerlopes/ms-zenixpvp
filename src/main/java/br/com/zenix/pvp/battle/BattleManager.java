package br.com.zenix.pvp.battle;

import org.bukkit.entity.Player;

import br.com.zenix.pvp.battle.onevsone.managements.constructor.BattleConstructor;
import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.managers.constructor.Management;

public class BattleManager extends Management {

	private final FightMode fightMode = new FightMode();

	public BattleManager(Manager manager) {
		super(manager);
	}

	public boolean initialize() {
		return true;
	}

	public FightMode getFight() {
		return fightMode;
	}

	public String getFighter(Player player) {
		if (!getFight().isFighting(player.getUniqueId())) {
			return "Ninguém";
		}

		FightMode sujectFight = getFight();
		BattleConstructor sujectBattle = sujectFight.getBattle(player.getUniqueId());
		String toTranslate = sujectBattle.getOpponent().getName();

		String finalResult = "NOT-FOUND";
		if (finalResult.equals("NOT-FOUND")) {
			if (toTranslate.length() == 16) {
				String shorts = toTranslate.substring(0, toTranslate.length() - 2);
				finalResult = shorts;
			} else if (toTranslate.length() == 15) {
				String shorts = toTranslate.substring(0, toTranslate.length() - 1);
				finalResult = shorts;
			} else {
				finalResult = toTranslate;
			}
		}

		return finalResult;
	}

}
