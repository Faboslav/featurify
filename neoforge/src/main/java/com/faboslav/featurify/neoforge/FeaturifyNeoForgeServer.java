package com.faboslav.featurify.neoforge;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

//? if >= 1.21.5 {
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
//?} else {
//import net.neoforged.neoforge.event.AddReloadListenerEvent;
//?}

public final class FeaturifyNeoForgeServer
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		eventBus.addListener(EventPriority.LOWEST, FeaturifyNeoForgeServer::onAddServerReloadListeners);
	}

	//? if >= 1.21.5 {
	private static void onAddServerReloadListeners(final AddServerReloadListenersEvent event) {
		var key = Featurify.makeNamespacedId("load_config");

		event.addListener(key, new SimplePreparableReloadListener<Void>() {
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
			if (!otherKey.equals(key)) {
				event.addDependency(otherKey, key);
			}
		}
	}
	//?} else {
	/*private static void onAddServerReloadListeners(final AddReloadListenerEvent event) {
		event.addListener(new SimplePreparableReloadListener<Void>() {
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
}
