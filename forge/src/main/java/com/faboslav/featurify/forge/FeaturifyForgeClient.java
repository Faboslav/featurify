package com.faboslav.featurify.forge;

import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings({"all", "deprecated", "removal"})
public final class FeaturifyForgeClient
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		FeaturifyClient.init();

		modEventBus.addListener(FeaturifyForgeClient::onClientSetup);
		modEventBus.addListener(FeaturifyForgeClient::onRegisterClientReloadListeners);
	}

	private static void onRegisterClientReloadListeners(final RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new SimplePreparableReloadListener<Void>() {
			@Override
			protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
				return null;
			}

			@Override
			protected void apply(Void result, ResourceManager resourceManager, ProfilerFiller profiler) {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
			}
		});
	}

	private static void onClientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory(
					(mc, screen) -> FeaturifyClient.getConfigScreen(screen)
				)
			);
		});
	}
}
