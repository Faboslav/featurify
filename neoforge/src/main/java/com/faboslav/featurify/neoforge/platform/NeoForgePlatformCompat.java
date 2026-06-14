package com.faboslav.featurify.neoforge.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformCompat;

public final class NeoForgePlatformCompat implements PlatformCompat
{
	@Override
	public void setupPlatformModCompat() {
		String modId = "";

		try {
		} catch (Throwable e) {
			Featurify.getLogger().error("Failed to setup compat with " + modId);
			e.printStackTrace();
		}
	}
}
