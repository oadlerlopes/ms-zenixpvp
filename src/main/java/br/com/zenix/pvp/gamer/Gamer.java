package br.com.zenix.pvp.gamer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import br.com.zenix.core.spigot.player.account.Account;
import br.com.zenix.pvp.PvP;
import br.com.zenix.pvp.kits.type.KitType;
import br.com.zenix.pvp.managers.Manager;
import br.com.zenix.pvp.warps.type.WarpType;

/**
 * Copyright (C) Zenix, all rights reserved unauthorized copying of this file,
 * via any medium is strictly prohibited proprietary and confidential
 */
public class Gamer {

	private UUID uniqueId;

	private final Account account;

	private WarpType warp;

	private boolean pvp = false;
	private boolean spectate = false;
	private Player spectatePlayer;

	private long waitTime = 0, inCombat = 0;

	private boolean scoreboard;
	private KitType kitType;
	
	public Gamer(Account account) {
		this.account = account;
		this.uniqueId = account.getUniqueId();

		scoreboard = false;
		this.spectate = false;

		warp = WarpType.NONE;
		kitType = KitType.NONE;
	}

	public Player getSpectatePlayer() {
		return spectatePlayer;
	}

	public void setSpectatePlayer(Player spectatePlayer) {
		this.spectatePlayer = spectatePlayer;
	}

	private Manager getManager() {
		return PvP.getManager();
	}

	public void update() {
		getManager().getGamerManager().forceUpdate(getAccount());
	}

	public boolean isSpectate() {
		return spectate;
	}

	public void setSpectate(boolean spectate) {
		this.spectate = spectate;
	}

	public KitType getKit() {
		return kitType;
	}

	public WarpType getWarp() {
		return warp;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public boolean isScoreboard() {
		return scoreboard;
	}

	public long getWaitTime() {
		long time = waitTime - System.currentTimeMillis();
		return time > 0 ? time : 0;
	}

	public boolean inWaitTime() {
		return waitTime > System.currentTimeMillis();
	}

	public void addWait(int seconds) {
		waitTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
	}

	public void removeWaitTime() {
		waitTime = 0;
	}

	public boolean hasPvP() {
		return pvp;
	}

	public void setPvP(boolean pvp) {
		setPvPByMessage(pvp, false);
	}

	public void setWarp(WarpType warp) {
		this.warp = warp;
	}

	public void setKit(KitType kitType) {
		this.kitType = kitType;
	}

	public void setPvPByMessage(boolean pvp, boolean message) {
		if (this.pvp == pvp)
			return;

		this.pvp = pvp;

		if (!message || account.getPlayer() == null)
			return;
	}

	public boolean inCombat() {
		return TimeUnit.MILLISECONDS.toSeconds(inCombat - System.currentTimeMillis()) > 0;
	}

	public void setCombat(int seconds) {
		inCombat = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
	}

	public void setScoreboard(boolean scoreboard) {
		this.scoreboard = scoreboard;
	}

	public int getTime() {
		return (int) TimeUnit.MILLISECONDS.toSeconds(getWaitTime());
	}

	public Account getAccount() {
		return account;
	}

}
