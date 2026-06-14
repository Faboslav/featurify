package com.faboslav.featurify.fabric;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.commands.FeaturifyCommands;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;

public final class FeaturifyFabric implements ModInitializer
{
	@Override
	public void onInitialize() {
		Featurify.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, dedicated) -> FeaturifyCommands.createCommand(dispatcher, buildContext));
		CommonLifecycleEvents.TAGS_LOADED.register(this::onDatapackReload);
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStart);
	}

	private void onDatapackReload(RegistryAccess registryAccess, boolean isClient) {
		if (isClient) {
			return;
		}

		RegistryManagerProvider.setRegistryManager(registryAccess);
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}

	private void onServerStart(MinecraftServer minecraftServer) {
		RegistryManagerProvider.setRegistryManager(minecraftServer.registryAccess());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
