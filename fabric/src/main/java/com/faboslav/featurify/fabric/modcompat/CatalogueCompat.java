package com.faboslav.featurify.fabric.modcompat;

import com.faboslav.featurify.common.FeaturifyClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.Screen;

public final class CatalogueCompat
{
	public static Screen createConfigScreen(Screen currentScreen, ModContainer container) {
		if (
			!FabricLoader.getInstance().isModLoaded("catalogue")
		) {
			return null;
		}

		return FeaturifyClient.getConfigScreen(currentScreen);
	}
}
