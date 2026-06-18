package com.faboslav.featurify.neoforge;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.commands.FeaturifyCommands;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.neoforge.registry.FeaturifyBiomeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@Mod(Featurify.MOD_ID)
public final class FeaturifyNeoForge
{
	public FeaturifyNeoForge(ModContainer modContainer, IEventBus modEventBus) {
		var eventBus = NeoForge.EVENT_BUS;

		Featurify.init();
		FeaturifyBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);

		//? if >= 1.21.9 {
		if (FMLEnvironment.getDist() == Dist.CLIENT)
			//?} else {
			/*if (FMLEnvironment.dist == Dist.CLIENT)
			 *///?}
		{
			FeaturifyNeoForgeClient.init(modEventBus, eventBus);
		}

		eventBus.addListener(FeaturifyNeoForge::registerCommand);
		eventBus.addListener(EventPriority.LOWEST, FeaturifyNeoForge::onResourceManagerReload);
		eventBus.addListener(EventPriority.LOWEST, FeaturifyNeoForge::onServerAboutToStart);
	}

	private static void registerCommand(RegisterCommandsEvent event) {
		FeaturifyCommands.createCommand(event.getDispatcher(), event.getBuildContext());
	}

	private static void onResourceManagerReload(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			return;
		}

		//? if >=1.21.3 {
		var registryAccess = event.getLookupProvider();
		//?} else {
		/*var registryAccess = event.getRegistryAccess();
		 *///?}

		RegistryManagerProvider.setRegistryManager(registryAccess);
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}

	private static void onServerAboutToStart(ServerAboutToStartEvent event) {
		RegistryManagerProvider.setRegistryManager(event.getServer().registryAccess());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
