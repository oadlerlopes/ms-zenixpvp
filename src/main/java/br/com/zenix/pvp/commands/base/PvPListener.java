package br.com.zenix.pvp.commands.base;

import org.bukkit.event.Listener;

import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.managers.Manager;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of
 * this file, via any medium is strictly prohibited proprietary and confidential
 */
public class PvPListener implements Listener {

	public Manager getManager() {
		return PvP.getManager();
	}

}
