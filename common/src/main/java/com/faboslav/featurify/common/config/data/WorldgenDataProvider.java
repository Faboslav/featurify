package com.faboslav.featurify.common.config.data;

import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

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

		/*
		for (var biomeTag : biomeRegistry.listTags().toList()) {
			biomes.add('#' + biomeTag.unwrapKey().get().location().toString());
		}*/

		for (var biome : biomeRegistry.listElements().toList()) {
			biomes.add(biome.unwrapKey().get()/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString());
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
			var placedFeatureId = placedFeatureReference.key()/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/;

			var defaultBiomes = new ArrayList<String>();

			for (var biomeReference : biomeRegistry.listElements().toList()) {
				if (getFeatureStep(biomeReference.value(), placedFeatureReference) == null) {
					continue;
				}

				String biomeId = biomeReference.key()/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString();

				if (!defaultBiomes.contains(biomeId)) {
					defaultBiomes.add(biomeId);
				}
			}

			var subFeaturesData = new HashMap<String, Float>();
			var subFeatures = placedFeature.getFeatures();

			for (var subFeatureReference : subFeatures.toList()) {
				if (subFeatureReference.value().config() instanceof RandomFeatureConfiguration config) {
					for (WeightedPlacedFeature weightedFeature : config.features) {
						var configuredFeatureKey = weightedFeature.feature.value()
							.feature()
							.unwrapKey()
							.orElse(null);

						if(configuredFeatureKey == null) {
							continue;
						}

						var subfeatureId = configuredFeatureKey.identifier();
						var weightedPlacedFeatureChance = weightedFeature.chance;

						subFeaturesData.put(subfeatureId.toString(), weightedPlacedFeatureChance);
					}
				}
			}

			PlacedFeatureData placedFeatureData = new PlacedFeatureData(defaultBiomes, subFeaturesData);

			placedFeatures.put(placedFeatureId.toString(), placedFeatureData);
		}

		return placedFeatures;
	}

	@Nullable
	public static GenerationStep.Decoration getFeatureStep(Biome biome, Holder<PlacedFeature> targetFeature) {
		var targetFeatureKey = targetFeature.unwrapKey().orElse(null);
		if (targetFeatureKey == null) {
			return null;
		}

		var decorations = GenerationStep.Decoration.values();
		var features = biome.getGenerationSettings().features();

		for (int stepIndex = 0; stepIndex < features.size(); stepIndex++) {
			if (stepIndex >= decorations.length) {
				continue;
			}

			for (Holder<PlacedFeature> feature : features.get(stepIndex)) {
				if (feature.is(targetFeatureKey)) {
					return decorations[stepIndex];
				}
			}
		}

		return null;
	}
}