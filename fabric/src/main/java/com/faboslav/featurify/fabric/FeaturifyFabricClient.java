package com.faboslav.featurify.fabric;

import com.faboslav.featurify.common.FeaturifyClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class FeaturifyFabricClient implements ClientModInitializer
{
	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		FeaturifyClient.init();
	}
}

