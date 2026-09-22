package com.faboslav.featurify.common.config;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;

public final class FeaturifyConfigSerializer
{
	public static void loadConfig(final LoadConfigEvent event) {
		if (Featurify.getConfig().isLoaded) {
			return;
		}

		Featurify.getConfig().load();

		if(event.updateRegistries()) {
			UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
		}
	}
}
