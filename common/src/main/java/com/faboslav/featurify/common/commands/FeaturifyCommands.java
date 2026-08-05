package com.faboslav.featurify.common.commands;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.network.packet.ConfigStatusToClientPacket;
import com.faboslav.featurify.common.network.packet.ConfigSyncRequestToClientPacket;
import com.faboslav.featurify.common.network.packet.ConfigSyncToClientPacket;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

//? if >= 1.21.11 {
/*import net.minecraft.server.permissions.Permissions;
*///?}

public final class FeaturifyCommands
{
	public static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		dispatcher.register(
			Commands.literal("featurify")
				//? if >= 1.21.11 {
				/*.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
				*///?} else {
				.requires(source -> source.hasPermission(2))
				 //?}
				.then(Commands.literal("dump")
					.executes(ctx -> {
						Featurify.getConfig().dump();
						ctx.getSource().sendSuccess(
							() -> Component.literal("Featurify config dumped to \"" + Featurify.getConfig().configDumpPath + "\"."),
							!ctx.getSource().isPlayer()
						);
						return 1;
					})
				)
				.then(Commands.literal("config")
					//? if >= 1.21.11 {
					/*.requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
					*///?} else {
					.requires(source -> source.hasPermission(4))
					 //?}
					.then(Commands.literal("sync")
						.then(Commands.literal("toServer")
							.executes(ctx -> syncConfigToServer(ctx.getSource()))
						)
						.then(Commands.literal("fromServer")
							.executes(ctx -> syncConfigFromServer(ctx.getSource()))
						)
					)
					.then(Commands.literal("status")
						.executes(ctx -> checkConfigStatus(ctx.getSource()))
					)
				)
		);
	}

	private static int syncConfigToServer(CommandSourceStack source) {
		var player = source.getPlayer();

		if (player == null) {
			source.sendFailure(Component.literal("This command can only be executed by a player."));
			return 0;
		}

		ConfigSyncRequestToClientPacket.sendToClient(player);

		source.sendSuccess(
			() -> Component.literal("Syncing your local Featurify config to server..."),
			false
		);

		return 1;
	}

	private static int syncConfigFromServer(CommandSourceStack source) {
		var player = source.getPlayer();

		if (player == null) {
			source.sendFailure(Component.literal("This command can only be executed by a player."));
			return 0;
		}

		ConfigSyncToClientPacket.sendToClient(Featurify.getConfig(), player, true);

		return 1;
	}

	private static int checkConfigStatus(CommandSourceStack source) {
		var player = source.getPlayer();

		if (player == null) {
			source.sendFailure(Component.literal("This command can only be executed by a player."));
			return 0;
		}

		ConfigStatusToClientPacket.sendToClient(Featurify.getConfig(), player);

		return 1;
	}
}