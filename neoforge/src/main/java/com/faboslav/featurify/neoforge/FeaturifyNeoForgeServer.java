package com.faboslav.featurify.neoforge;

import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

public final class FeaturifyNeoForgeServer
{
	public static void init(IEventBus modEventBus, IEventBus eventBus) {
		modEventBus.addListener(FeaturifyNeoForgeServer::onLoadComplete);
	}

	private static void onLoadComplete(final FMLLoadCompleteEvent event) {
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
	}
}
