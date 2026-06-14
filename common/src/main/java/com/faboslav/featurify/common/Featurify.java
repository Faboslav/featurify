package com.faboslav.featurify.common;

import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.FeaturifyConfigSerializer;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.modcompat.ModChecker;
import com.faboslav.featurify.common.registry.RegistryUpdater;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"all", "removal"})
public final class Featurify
{
	public static final String MOD_ID = "featurify";
	private static final Logger LOGGER = LoggerFactory.getLogger(Featurify.MOD_ID);
	private static final FeaturifyConfig CONFIG = new FeaturifyConfig();

	public static FeaturifyConfig getConfig() {
		return CONFIG;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static ResourceLocation makeId(String path) {
		//? if >=1.21 {
		/*return ResourceLocation.tryBuild(
			MOD_ID,
			path
		);
		*///?} else {
		return new ResourceLocation(
			MOD_ID,
			path
		);
		//?}
	}

	public static ResourceLocation makeId(String namespace, String path) {
		//? if >=1.21 {
		/*return ResourceLocation.tryBuild(
			namespace,
			path
		);
		*///?} else {
		return new ResourceLocation(
			namespace,
			path
		);
		//?}
	}

	public static ResourceLocation makeNamespacedId(String id) {
		//? if >=1.21 {
		/*return ResourceLocation.parse(
			id
		);
		*///?} else {
		return new ResourceLocation(
			id
		);
		//?}
	}

	public static String makeStringID(String name) {
		return MOD_ID + ":" + name;
	}

	public static void init() {
		Featurify.getConfig().create();
		ModChecker.setupModCompat();

		LoadConfigEvent.EVENT.addListener(FeaturifyConfigSerializer::loadConfig);
		UpdateRegistriesEvent.EVENT.addListener(RegistryUpdater::updateRegistries);
	}
}