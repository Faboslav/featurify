package com.faboslav.featurify.common.config.data;

import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import com.faboslav.featurify.common.util.FeatureUtil;
import com.faboslav.featurify.common.versions.VersionedId;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.*;

public final class WorldgenDataProvider
{
	private static List<String> biomes = new ArrayList<>();
	private static Map<String, PlacedFeatureData> placedFeatures = new TreeMap<>();

	public static List<String> getBiomes() {
		return biomes;
	}

	public static Map<String, PlacedFeatureData> getPlacedFeatures() {
		return placedFeatures;
	}

	public static void loadWorldgenData() {
		biomes = loadBiomes();
		placedFeatures = loadPlacedFeatures();
	}

	public static List<String> loadBiomes() {
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return Collections.emptyList();
		}

		List<String> biomes = new ArrayList<>();

		for (var biome : biomeRegistry.listElements().toList()) {
			biomes.add(VersionedId.GetId(biome.unwrapKey().get()).toString());
		}

		return biomes;
	}

	public static Map<String, PlacedFeatureData> loadPlacedFeatures() {
		var placedFeatureRegistry = RegistryManagerProvider.getPlacedFeatureRegistry();

		if (placedFeatureRegistry == null) {
			return Collections.emptyMap();
		}

		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return Collections.emptyMap();
		}

		Map<String, PlacedFeatureData> placedFeatures = new TreeMap<>(Comparators.ALPHABETICALL_ID_COMPARATOR);

		for (var placedFeatureReference : placedFeatureRegistry.listElements().toList()) {
			PlacedFeature placedFeature = placedFeatureReference.value();
			var placedFeatureId = VersionedId.GetId(placedFeatureReference.key());

			var defaultBiomes = new ArrayList<String>();

			for (var biomeReference : biomeRegistry.listElements().toList()) {
				if (FeatureUtil.getFeatureStep(biomeReference.value(), placedFeatureReference) == null) {
					continue;
				}

				String biomeId = VersionedId.GetId(biomeReference.key()).toString();

				if (!defaultBiomes.contains(biomeId)) {
					defaultBiomes.add(biomeId);
				}
			}

			defaultBiomes.sort(Comparators.ALPHABETICALL_ID_COMPARATOR);

			var subFeaturesData = new TreeMap<String, Float>(Comparators.ALPHABETICALL_ID_COMPARATOR);
			var randomFeatureConfigurations = new ArrayList<RandomFeatureConfiguration>();

			FeatureUtil.collectRandomFeatureConfigurations(
				placedFeature,
				Collections.newSetFromMap(new IdentityHashMap<>()),
				randomFeatureConfigurations
			);

			for (RandomFeatureConfiguration config : randomFeatureConfigurations) {
				//? if >= 26.2 {
				var features = config.features();
				//?} else {
				/*var features = config.features;
				 *///?}

				for (WeightedPlacedFeature weightedFeature : features) {
					//? if >= 26.2 {
					var configuredFeatureKey = weightedFeature.feature().value().feature().unwrapKey().orElse(null);
					var weightedPlacedFeatureChance = weightedFeature.chance();
					//?} else {
					/*var configuredFeatureKey = weightedFeature.feature.value().feature().unwrapKey().orElse(null);
					var weightedPlacedFeatureChance = weightedFeature.chance;
					*///?}

					if (configuredFeatureKey == null) {
						continue;
					}

					var subfeatureId = VersionedId.GetId(configuredFeatureKey);
					subFeaturesData.put(subfeatureId.toString(), weightedPlacedFeatureChance);
				}
			}

			PlacedFeatureData placedFeatureData = new PlacedFeatureData(defaultBiomes, subFeaturesData);
			placedFeatures.put(placedFeatureId.toString(), placedFeatureData);
		}

		return placedFeatures;
	}
}