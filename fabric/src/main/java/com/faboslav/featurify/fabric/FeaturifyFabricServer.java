package com.faboslav.featurify.fabric;

import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.fabricmc.api.DedicatedServerModInitializer;

public final class FeaturifyFabricServer implements DedicatedServerModInitializer
{
	@Override
	public void onInitializeServer() {
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
