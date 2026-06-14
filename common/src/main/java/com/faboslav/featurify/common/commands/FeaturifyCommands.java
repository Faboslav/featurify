package com.faboslav.featurify.common.commands;

import com.faboslav.featurify.common.Featurify;
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
		);
	}
}