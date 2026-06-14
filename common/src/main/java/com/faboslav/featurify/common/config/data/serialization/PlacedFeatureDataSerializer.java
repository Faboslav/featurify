package com.faboslav.featurify.common.config.data.serialization;

import com.faboslav.featurify.common.config.data.PlacedFeatureData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public final class PlacedFeatureDataSerializer
{
	public static final String NAME_PROPERTY = "name";
	private static final String IS_DISABLED_PROPERTY = "is_disabled";
	private static final String WHITELISTED_BIOMES_PROPERTY = "whitelisted_biomes";
	private static final String BLACKLISTED_BIOMES_PROPERTY = "blacklisted_biomes";

	public static void load(JsonObject placedFeatureJson, PlacedFeatureData placedFeatureData) {
		var placedFeatureName = placedFeatureJson.get(NAME_PROPERTY).getAsString();

		if (placedFeatureJson.has(IS_DISABLED_PROPERTY)) {
			placedFeatureData.setDisabled(placedFeatureJson.get(IS_DISABLED_PROPERTY).getAsBoolean());
		}

		if (placedFeatureJson.has(WHITELISTED_BIOMES_PROPERTY) || placedFeatureJson.has(BLACKLISTED_BIOMES_PROPERTY)) {
			List<String> biomes = new ArrayList<>(placedFeatureData.getDefaultBiomes());

			if(placedFeatureJson.has(WHITELISTED_BIOMES_PROPERTY)) {
				var whitelistedBiomes = placedFeatureJson.getAsJsonArray(WHITELISTED_BIOMES_PROPERTY);
				for (JsonElement whitelistedBiome : whitelistedBiomes) {
					if (biomes.contains(whitelistedBiome.getAsString())) {
						continue;
					}

					biomes.add(whitelistedBiome.getAsString());
				}
			}

			if(placedFeatureJson.has(BLACKLISTED_BIOMES_PROPERTY)) {
				var blacklistedBiomes = placedFeatureJson.getAsJsonArray(BLACKLISTED_BIOMES_PROPERTY);

				for (JsonElement blacklistedBiome : blacklistedBiomes) {
					if (!biomes.contains(blacklistedBiome.getAsString())) {
						continue;
					}

					biomes.remove(blacklistedBiome.getAsString());
				}
			}

			placedFeatureData.setBiomes(biomes);
		}
	}

	public static void save(JsonArray placedFeaturesJson, String placedFeatureName, PlacedFeatureData placedFeatureData) {
		JsonObject placedFeature = new JsonObject();

		placedFeature.addProperty(NAME_PROPERTY, placedFeatureName);

		if(!placedFeatureData.isUsingDefaultIsDisabled()) {
			placedFeature.addProperty(IS_DISABLED_PROPERTY, placedFeatureData.isDisabled());
		}

		var whitelistedBiomes = new ArrayList<>(placedFeatureData.getBiomes());
		whitelistedBiomes.removeAll(placedFeatureData.getDefaultBiomes());

		if (!whitelistedBiomes.isEmpty()) {
			JsonArray whitelistedBiomesJson = new JsonArray();
			whitelistedBiomes.stream().distinct().forEach(whitelistedBiomesJson::add);
			placedFeature.add(WHITELISTED_BIOMES_PROPERTY, whitelistedBiomesJson);
		}

		var blacklistedBiomes = new ArrayList<>(placedFeatureData.getDefaultBiomes());
		blacklistedBiomes.removeAll(placedFeatureData.getBiomes());

		if(!blacklistedBiomes.isEmpty()) {
			JsonArray blacklistedBiomesJson = new JsonArray();
			blacklistedBiomes.stream().distinct().forEach(blacklistedBiomesJson::add);
			placedFeature.add(BLACKLISTED_BIOMES_PROPERTY, blacklistedBiomesJson);
		}

		placedFeaturesJson.add(placedFeature);
	}
}
