package com.faboslav.featurify.neoforge;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

//? if >= 1.21.5 {
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
//?} else {
//import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
//?}

//? if <1.20.6 {
//import net.neoforged.neoforge.client.ConfigScreenHandler;

 //?} else {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?}

public final class FeaturifyNeoForgeClient
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		FeaturifyClient.init();

		modEventBus.addListener(FeaturifyNeoForgeClient::onClientSetup);
		modEventBus.addListener(EventPriority.LOWEST, FeaturifyNeoForgeClient::onRegisterClientReloadListeners);
	}

	//? if >= 1.21.5 {
	private static void onRegisterClientReloadListeners(final AddClientReloadListenersEvent event) {
		var loadConfigId = Featurify.makeNamespacedId("load_config");

		event.addListener(loadConfigId, new SimplePreparableReloadListener<Void>() {
			@Override
			protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
				return null;
			}

			@Override
			protected void apply(Void result, ResourceManager resourceManager, ProfilerFiller profiler) {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
			}
		});

		for (var otherKey : event.getRegistry().keySet()) {
			if (!otherKey.equals(loadConfigId)) {
				event.addDependency(otherKey, loadConfigId);
			}
		}
	}
	//?} else {
	/*private static void onRegisterClientReloadListeners(final RegisterClientReloadListenersEvent event) {
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
	*///?}

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
