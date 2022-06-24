package br.com.zenix.pvp.battle.onevsone.modules;

public enum BattleModules {

	NORMAL("Normal", 8),
	FAST("Fast", 8),
	CUSTOM("Customizado", 36);

	private int soups;
	private String name;

	BattleModules(String name, int soups) {
		this.soups = soups;
		this.name = name;
	}

	public int getSoups() {
		return soups;
	}

	public String getName() {
		return name;
	}

}
