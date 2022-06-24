package br.com.zenix.pvp.commands.player.warp;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.zenix.pvp.commands.base.PvPCommand;
import br.com.zenix.pvp.managers.managements.ConfigManager;
import br.com.zenix.pvp.warps.type.WarpType;

public class SetupCommand extends PvPCommand {

	public SetupCommand() {
		super("setup");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (!isPlayer(sender) || !hasPermission(sender, "setup"))
			return true;

		Player player = (Player) sender;
		Location location = player.getLocation();
		ConfigManager config = getManager().getConfigManager();

		if (args.length == 1) {
			if (WarpType.contains(args[0])) {
				if (check(args, 0, "spawn") || check(args, 0, "arena") || check(args, 0, "fps") || check(args, 0, "simulator") || check(args, 0, "mlg") || check(args, 0, "main")) {
					sendHelpMessage(player);
					return true;
				}
				WarpType warp = WarpType.getFromString(args[0].toLowerCase());
				config.registerInConfig(player, "Warps." + warp);
				player.sendMessage("");
				player.sendMessage("Você fixou a warp §a" + warp.getName() + "§7.");
				player.sendMessage("Coordenadas §e" + getCoordinates(location) + "§7.");
				player.sendMessage("");
			} else if (!args[0].equalsIgnoreCase("1v1")) {
				player.sendMessage("§cA warp §n" + args[0] + "§c não foi encontrada!");
			} else {
				player.sendMessage("/setup 1v1 (pos1, pos2, spawn).");
			}
			return true;
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("1v1")) {
				if (check(args, 1, "pos1") || check(args, 1, "pos2") || check(args, 1, "spawn")) {
					config.registerInConfig(player, "1v1." + args[1]);
					player.sendMessage("");
					player.sendMessage("Você fixou §6" + args[1] + "§7 da warp §a1v1§7.");
					player.sendMessage("Coordenadas §e" + getCoordinates(location) + "§7.");
					player.sendMessage("");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("feast")) {
				if (args[1].equalsIgnoreCase("pos1") || args[1].equalsIgnoreCase("pos2")) {
					config.registerInConfig(player, "Warps.Feast." + args[1]);
					player.sendMessage("");
					player.sendMessage("Você fixou §6" + args[1] + "§7 do §aFeast§7.");
					player.sendMessage("Coordenadas §e" + getCoordinates(location) + "§7.");
					player.sendMessage("");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("simulatorfeast")) {
				if (args[1].equalsIgnoreCase("pos1") || args[1].equalsIgnoreCase("pos2")) {
					config.registerInConfig(player, "Warps.SimulatorFeast." + args[1]);
					player.sendMessage("");
					player.sendMessage("Você fixou §6" + args[1] + "§7 do §aHG Simulator§7.");
					player.sendMessage("Coordenadas §e" + getCoordinates(location) + "§7.");
					player.sendMessage("");
				}
				return true;
			} else {
				sendHelpMessage(player);
				return true;
			}
		}

		sendHelpMessage(player);
		return true;
	}

	private void sendHelpMessage(Player player) {
		player.sendMessage("§cUso correto do comando Setup:");
		player.sendMessage("§a/setup (warp) §7- Fixe as warps LAVA e POTION.");
		player.sendMessage("§a/setup (warp) (altura) §7- Fixe a warps MLG.");
		player.sendMessage("§a/setup (warp) (altura) (raio) §7- Fixe as warps FPS, MAIN, SIMULATOR e ARENA.");
		player.sendMessage("§a/setup (1v1) (spawn/pos1/pos2) §7- Fixe as posições da warp 1V1.");
		player.sendMessage("§a/setup (feast/simulatorFeast) (POS1/POS2) §7- Fixe as posições dos baús do feast (Arena/Simulator).");
		player.sendMessage("§a/setup (evento) (rdm) (spawn) (spawnId) §7- Fixe as posições dos jogadores no evento rdm.");
		player.sendMessage("§a/setup (evento) (rdm) (lobby) §7- Fixe a posiçõe do lobly do evento rdm.");
	}

	private boolean check(String[] arguments, Integer id, String equals) {
		return arguments[id].equalsIgnoreCase(equals);
	}

	private String getCoordinates(Location location) {
		return "X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ();
	}

}
