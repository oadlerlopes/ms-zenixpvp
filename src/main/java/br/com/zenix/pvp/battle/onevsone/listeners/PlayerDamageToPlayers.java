package br.com.zenix.pvp.battle.onevsone.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.zenix.pvp.battle.onevsone.managements.containers.FightMode;
import br.com.zenix.pvp.commands.base.PvPListener;
import br.com.zenix.pvp.gamer.Gamer;
import br.com.zenix.pvp.warps.type.WarpType;

public class PlayerDamageToPlayers extends PvPListener {

	@EventHandler
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
			return;

		FightMode fightMode = getManager().getOneVsOneManager().getFight();

		Entity entity = event.getEntity();
		Entity damager = event.getDamager();

		Player playerEntity = (Player) entity;
		Player playerDamager = (Player) damager;

		Gamer gamerEntity = getManager().getGamerManager().getGamer(playerEntity);
		if (gamerEntity.getWarp().equals(WarpType.ONE_VS_ONE)) {
			if (!fightMode.isFighting(gamerEntity.getUniqueId())) {
				event.setCancelled(true);
			} else if (!fightMode.getFighters().get(gamerEntity.getUniqueId()).getOpponentUUID()
					.equals(damager.getUniqueId())) {
				event.setCancelled(true);
			}
		}

		if (gamerEntity.getWarp().equals(WarpType.LAVA)) {
			event.setCancelled(true);
		}
		
		Gamer gamerDamager = getManager().getGamerManager().getGamer(playerDamager);
		if (gamerDamager.getWarp().equals(WarpType.ONE_VS_ONE)) {
			if (!fightMode.isFighting(gamerDamager.getUniqueId())) {
				event.setCancelled(true);
			} else if (!fightMode.getFighters().get(gamerDamager.getUniqueId()).getOpponentUUID().equals(entity.getUniqueId())) {
				event.setCancelled(true);
			}
		}
		
		if (gamerDamager.getWarp().equals(WarpType.LAVA)) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (getManager().getGamerManager().getGamer(event.getEntity().getUniqueId()).getWarp()
				.equals(WarpType.ONE_VS_ONE)) {
			if (event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.FIRE)
					|| event.getCause().equals(DamageCause.FIRE_TICK))
				event.setCancelled(true);
		}

	}

}
