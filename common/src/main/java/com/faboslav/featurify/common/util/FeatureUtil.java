package com.faboslav.featurify.common.util;

import com.faboslav.featurify.common.Featurify;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class FeatureUtil
{
	public static void collectRandomFeatureConfigurations(
		PlacedFeature placedFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		List<RandomFeatureConfiguration> configs
	) {
		collectRandomFeatureConfigurations(placedFeature, visitedPlacedFeatures, configs, null);
	}

	public static void collectRandomFeatureConfigurations(
		PlacedFeature placedFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		List<RandomFeatureConfiguration> configs,
		@Nullable List<Holder<PlacedFeature>> usedPlacedFeatures
	) {
		collectRandomFeatureConfigurations(
			placedFeature,
			visitedPlacedFeatures,
			Collections.newSetFromMap(new IdentityHashMap<>()),
			configs,
			usedPlacedFeatures
		);
	}

	private static void collectRandomFeatureConfigurations(
		PlacedFeature placedFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		Set<Object> visitedConfiguredFeatures,
		List<RandomFeatureConfiguration> configs,
		@Nullable List<Holder<PlacedFeature>> usedPlacedFeatures
	) {
		if (!visitedPlacedFeatures.add(placedFeature)) {
			return;
		}

		collectRandomFeatureConfigurations(placedFeature.feature().value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs, usedPlacedFeatures);
	}

	private static void collectRandomFeatureConfigurations(
		Object configuredFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		Set<Object> visitedConfiguredFeatures,
		List<RandomFeatureConfiguration> configs,
		@Nullable List<Holder<PlacedFeature>> usedPlacedFeatures
	) {
		try {
			if (!visitedConfiguredFeatures.add(configuredFeature)) {
				return;
			}

			var config = ((net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>) configuredFeature).config();

			if (config instanceof RandomFeatureConfiguration randomFeatureConfiguration) {
				configs.add(randomFeatureConfiguration);

				//? if >= 26.2 {
				var features = randomFeatureConfiguration.features();
				var defaultFeature = randomFeatureConfiguration.defaultFeature();
				 //?} else {
				/*var features = randomFeatureConfiguration.features;
				var defaultFeature = randomFeatureConfiguration.defaultFeature;
				*///?}

				for (WeightedPlacedFeature weightedPlacedFeature : features) {
					//? if >= 26.2 {
					var subPlacedFeature = weightedPlacedFeature.feature();
					 //?} else {
					/*var subPlacedFeature = weightedPlacedFeature.feature;
					*///?}

					if (usedPlacedFeatures != null) {
						usedPlacedFeatures.add(subPlacedFeature);
					}

					collectRandomFeatureConfigurations(subPlacedFeature.value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs, usedPlacedFeatures);
				}

				if (usedPlacedFeatures != null) {
					usedPlacedFeatures.add(defaultFeature);
				}

				collectRandomFeatureConfigurations(defaultFeature.value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs, usedPlacedFeatures);

				return;
			}

			//? if >= 26.1 {
			config.getSubFeatures().forEach(childConfiguredFeature -> {
				collectRandomFeatureConfigurations(childConfiguredFeature.value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs, usedPlacedFeatures);
			});
			//?} else {
			/*config.getFeatures().forEach(childConfiguredFeature -> {
				collectRandomFeatureConfigurations(childConfiguredFeature, visitedPlacedFeatures, visitedConfiguredFeatures, configs, usedPlacedFeatures);
			});
			*///?}
		} catch (Throwable e) {
			Featurify.getLogger().error("Unable to collect random feature configurations", e);
		}
	}

	@Nullable
	public static GenerationStep.Decoration getFeatureStep(
		HolderLookup.RegistryLookup<Biome> biomeRegistry,
		Holder<PlacedFeature> targetFeature
	) {
		for (var biomeReference : biomeRegistry.listElements().toList()) {
			var step = getFeatureStep(biomeReference.value(), targetFeature);

			if (step != null) {
				return step;
			}
		}

		return null;
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
