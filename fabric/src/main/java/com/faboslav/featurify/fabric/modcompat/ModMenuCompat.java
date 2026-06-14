package com.faboslav.featurify.fabric.modcompat;

import com.faboslav.featurify.common.FeaturifyClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public final class ModMenuCompat implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (screen) -> {
			if (
				!FabricLoader.getInstance().isModLoaded("modmenu")
			) {
				return null;
			}

			return FeaturifyClient.getConfigScreen(screen);
		};
	}
}