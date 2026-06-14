package com.faboslav.featurify.common;

import com.faboslav.featurify.common.config.client.gui.FeaturifyConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public final class FeaturifyClient
{
	private static final FeaturifyConfigScreen CONFIG_SCREEN = new FeaturifyConfigScreen();

	public static void init() {
	}

	public static Screen getConfigScreen(Screen screen) {
		return CONFIG_SCREEN.generateScreen(screen);
	}

	@Nullable
	public static FeaturifyConfigScreen getConfigScreen() {
		return CONFIG_SCREEN;
	}
}
