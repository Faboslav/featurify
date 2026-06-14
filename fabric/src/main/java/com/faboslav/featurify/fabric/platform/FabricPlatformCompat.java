package com.faboslav.featurify.fabric.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformCompat;
import com.faboslav.featurify.fabric.modcompat.GlobalDatapacksCompat;

import static com.faboslav.featurify.common.modcompat.ModChecker.loadModCompat;

public final class FabricPlatformCompat implements PlatformCompat
{
	@Override
	public void setupPlatformModCompat() {
		String modId = "";

		try {
			modId = "global-datapack";
			loadModCompat(modId, () -> new GlobalDatapacksCompat());
		} catch (Throwable e) {
			Featurify.getLogger().error("Failed to setup compat with " + modId);
			e.printStackTrace();
		}
	}
}
