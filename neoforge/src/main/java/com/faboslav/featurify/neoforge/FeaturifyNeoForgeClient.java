package com.faboslav.featurify.neoforge;

import com.faboslav.featurify.common.FeaturifyClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

//? if <1.20.6 {
/*import net.neoforged.neoforge.client.ConfigScreenHandler;

 *///?} else {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?}

public final class FeaturifyNeoForgeClient
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		FeaturifyClient.init();

		modEventBus.addListener(FeaturifyNeoForgeClient::onClientSetup);
	}

	private static void onClientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			//? if <1.20.6 {
			/*ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory(
					(mc, screen) -> FeaturifyClient.getConfigScreen(screen)
				)
			);
			*///?} else {
			ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, screen) -> {
				return FeaturifyClient.getConfigScreen(screen);
			});
			//?}
		});
	}
}
