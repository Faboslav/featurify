package com.faboslav.featurify.common.util;

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
		collectRandomFeatureConfigurations(
			placedFeature,
			visitedPlacedFeatures,
			Collections.newSetFromMap(new IdentityHashMap<>()),
			configs
		);
	}

	private static void collectRandomFeatureConfigurations(
		PlacedFeature placedFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		Set<Object> visitedConfiguredFeatures,
		List<RandomFeatureConfiguration> configs
	) {
		if (!visitedPlacedFeatures.add(placedFeature)) {
			return;
		}

		collectRandomFeatureConfigurations(placedFeature.feature().value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs);
	}

	private static void collectRandomFeatureConfigurations(
		Object configuredFeature,
		Set<PlacedFeature> visitedPlacedFeatures,
		Set<Object> visitedConfiguredFeatures,
		List<RandomFeatureConfiguration> configs
	) {
		if (!visitedConfiguredFeatures.add(configuredFeature)) {
			return;
		}

		var config = ((net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>) configuredFeature).config();

		if (config instanceof RandomFeatureConfiguration randomFeatureConfiguration) {
			configs.add(randomFeatureConfiguration);

			//? if >= 26.2 {
			var features = randomFeatureConfiguration.features();
			 //?} else {
			/*var features = randomFeatureConfiguration.features;
			*///?}

			for (WeightedPlacedFeature weightedPlacedFeature : features) {
				//? if >= 26.2 {
				collectRandomFeatureConfigurations(weightedPlacedFeature.feature().value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs);
				 //?} else {
				/*collectRandomFeatureConfigurations(weightedPlacedFeature.feature.value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs);
				*///?}
			}

			return;
		}

		//? if >= 26.1 {
		config.getSubFeatures().forEach(childConfiguredFeature -> {
			collectRandomFeatureConfigurations(childConfiguredFeature.value(), visitedPlacedFeatures, visitedConfiguredFeatures, configs);
		});
		//?} else {
		/*config.getFeatures().forEach(childConfiguredFeature -> {
			collectRandomFeatureConfigurations(childConfiguredFeature, visitedPlacedFeatures, visitedConfiguredFeatures, configs);
		});
		*///?}
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