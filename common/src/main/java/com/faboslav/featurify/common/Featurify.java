package com.faboslav.featurify.common;

import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.FeaturifyConfigSerializer;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.modcompat.ModChecker;
import com.faboslav.featurify.common.registry.RegistryUpdater;
import net.minecraft.resources.Identifier;
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

	public static Identifier makeId(String path) {
		//? if >=1.21 {
		return Identifier.tryBuild(
			MOD_ID,
			path
		);
		//?} else {
		/*return new Identifier(
			MOD_ID,
			path
		);
		*///?}
	}

	public static Identifier makeId(String namespace, String path) {
		//? if >=1.21 {
		return Identifier.tryBuild(
			namespace,
			path
		);
		//?} else {
		/*return new Identifier(
			namespace,
			path
		);
		*///?}
	}

	public static Identifier makeNamespacedId(String id) {
		//? if >=1.21 {
		return Identifier.parse(
			id
		);
		//?} else {
		/*return new Identifier(
			id
		);
		*///?}
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