package com.faboslav.featurify.forge;

import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class FeaturifyForgeServer
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		eventBus.addListener(FeaturifyForgeServer::onAddReloadListener);
	}

	private static void onAddReloadListener(final AddReloadListenerEvent event) {
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
}
