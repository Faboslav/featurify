package com.faboslav.featurify.common.config.data.serialization;

import com.faboslav.featurify.common.config.data.BiomeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class BiomeDataSerializer
{
	public static final String NAME_PROPERTY = "name";
	private static final String IS_DISABLED_PROPERTY = "is_disabled";
	private static final String REPLACEMENT_BIOME_PROPERTY = "replacement_biome";

	public static void load(JsonObject biomesJson, BiomeData biomeData) {
		var biomeName = biomesJson.get(NAME_PROPERTY).getAsString();

		if (biomesJson.has(IS_DISABLED_PROPERTY)) {
			biomeData.setDisabled(biomesJson.get(IS_DISABLED_PROPERTY).getAsBoolean());
		}

		if (biomesJson.has(REPLACEMENT_BIOME_PROPERTY)) {
			biomeData.setReplacementBiome(biomesJson.get(REPLACEMENT_BIOME_PROPERTY).getAsString());
		}
	}

	public static void save(JsonArray biomesJson, String biomeName, BiomeData biomeData) {
		JsonObject biome = new JsonObject();

		biome.addProperty(NAME_PROPERTY, biomeName);

		if(!biomeData.isUsingDefaultIsDisabled()) {
			biome.addProperty(IS_DISABLED_PROPERTY, biomeData.isDisabled());
		}

		if(!biomeData.isUsingDefaultReplacementBiome()) {
			biome.addProperty(REPLACEMENT_BIOME_PROPERTY, biomeData.getReplacementBiome());
		}

		biomesJson.add(biome);
	}
}
