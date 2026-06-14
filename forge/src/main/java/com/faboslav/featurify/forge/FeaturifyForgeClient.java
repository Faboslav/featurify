package com.faboslav.featurify.forge;

import com.faboslav.featurify.common.FeaturifyClient;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@SuppressWarnings({"all", "deprecated", "removal"})
public final class FeaturifyForgeClient
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		FeaturifyClient.init();

		modEventBus.addListener(FeaturifyForgeClient::onClientSetup);
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
