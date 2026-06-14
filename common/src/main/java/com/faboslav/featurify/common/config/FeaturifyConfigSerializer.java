package com.faboslav.featurify.common.config;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;

public final class FeaturifyConfigSerializer
{
	public static void loadConfig(final LoadConfigEvent event) {
		if (Featurify.getConfig().isLoaded) {
			return;
		}

		Featurify.getConfig().load();
	}
}
