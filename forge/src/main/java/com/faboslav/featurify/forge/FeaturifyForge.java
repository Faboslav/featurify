package com.faboslav.featurify.forge;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.commands.FeaturifyCommands;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.forge.registry.FeaturifyBiomeModifiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Featurify.MOD_ID)
@SuppressWarnings({"all", "deprecated", "removal"})
public final class FeaturifyForge
{
	static int timer = 20;
	static boolean canUpdate = true;

	public FeaturifyForge() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus eventBus = MinecraftForge.EVENT_BUS;

		Featurify.init();
		FeaturifyBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			FeaturifyForgeClient.init(modEventBus, eventBus);
		}

		eventBus.addListener(FeaturifyForge::registerCommand);
		eventBus.addListener(FeaturifyForge::onResourceManagerReload);
		eventBus.addListener(FeaturifyForge::onServerAboutToStart);
	}

	private static void registerCommand(RegisterCommandsEvent event) {
		FeaturifyCommands.createCommand(event.getDispatcher(), event.getBuildContext());
	}

	private static void onResourceManagerReload(TagsUpdatedEvent event) {
		if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
			return;
		}

		RegistryManagerProvider.setRegistryManager(event.getRegistryAccess());
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}

	private static void onServerAboutToStart(ServerAboutToStartEvent event) {
		RegistryManagerProvider.setRegistryManager(event.getServer().registryAccess());
		UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
