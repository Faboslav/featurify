package com.faboslav.featurify.common.registry;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.api.FeaturifyPlacedFeature;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.platform.PlatformHooks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class RegistryUpdater
{
	public static void updateRegistries(final UpdateRegistriesEvent event) {
		if (!Featurify.getConfig().isLoaded) {
			Featurify.getLogger().info("Registries not updated, config not loaded");
			return;
		}

		try {
			Featurify.getLogger().info("Updating registries...");
			var registryManager = event.registryManager();

			if (registryManager == null) {
				Featurify.getLogger().info("Registries not updated, registry manager not loaded");
				return;
			}

			updatePlacedFeatures(registryManager);
			Featurify.getLogger().info("Registries updated");
		} catch (Exception e) {
			Featurify.getLogger().error("Failed to update registries");
			Featurify.getLogger().error(e.toString());
		}
	}

	private static void updatePlacedFeatures(HolderLookup.Provider registryManager) {
		var placedFeatureRegistry = registryManager.lookup(Registries.PLACED_FEATURE).orElse(null);
		var biomeRegistry = registryManager.lookup(Registries.BIOME).orElse(null);

		if (placedFeatureRegistry == null || biomeRegistry == null) {
			return;
		}

		List<Holder.Reference<Biome>> biomeReferences = biomeRegistry.listElements().toList();
		Map<ResourceKey<Biome>, List<HolderSet<PlacedFeature>>> simulatedFeatures = new HashMap<>();

		for (var placedFeatureReference : placedFeatureRegistry.listElements().toList()) {
			PlacedFeature placedFeature = placedFeatureReference.value();
			var placedFeatureRegistryKey = placedFeatureReference.key();
			Identifier placedFeatureId = placedFeatureRegistryKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/;

			var featurifyPlacedFeature = ((FeaturifyPlacedFeature) (Object) placedFeature);
			featurifyPlacedFeature.featurify$setIdentifier(placedFeatureId);

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().get(placedFeatureId.toString());

			if (placedFeatureData == null) {
				continue;
			}

			var additionalBiomes = placedFeatureData.getAdditionalBiomes();
			var removedBiomes = placedFeatureData.getRemovedBiomes();

			if (additionalBiomes.isEmpty() && removedBiomes.isEmpty()) {
				continue;
			}

			var knownStep = getFeatureStep(biomeRegistry, placedFeatureReference);

			if (knownStep == null) {
				continue;
			}

			for (var biomeReference : biomeReferences) {
				var biomeKey = biomeReference.unwrapKey().orElseThrow();
				String biomeId = biomeKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString();
				var currentFeatures = getFeaturesForBiome(simulatedFeatures, biomeReference);

				if (removedBiomes.contains(biomeId) && containsFeature(currentFeatures, placedFeatureReference)) {
					currentFeatures = createFeaturesWithRemovedFeature(currentFeatures, placedFeatureReference, knownStep);
					simulatedFeatures.put(biomeKey, currentFeatures);

					PlatformHooks.PLATFORM_BIOME_MODIFICATIONS.removePlacedFeature(
						placedFeatureReference,
						biomeReference,
						knownStep
					);
				}

				if (!additionalBiomes.contains(biomeId)) {
					continue;
				}

				if (containsFeature(currentFeatures, placedFeatureReference)) {
					continue;
				}

				if (!canSafelyAddPlacedFeatureToBiome(simulatedFeatures, placedFeatureReference, biomeReference, knownStep)) {
					continue;
				}

				currentFeatures = createFeaturesWithAddedFeature(currentFeatures, placedFeatureReference, knownStep);
				simulatedFeatures.put(biomeKey, currentFeatures);

				PlatformHooks.PLATFORM_BIOME_MODIFICATIONS.addPlacedFeature(
					placedFeatureReference,
					biomeReference,
					knownStep
				);
			}
		}

		Featurify.getLogger().info("Placed feature registries updated");
	}

	public static boolean canSafelyAddPlacedFeatureToBiome(
		Map<ResourceKey<Biome>, List<HolderSet<PlacedFeature>>> simulatedFeatures,
		Holder<PlacedFeature> placedFeatureReference,
		Holder<Biome> targetBiomeReference,
		GenerationStep.Decoration generationStep
	) {
		var targetBiomeKey = targetBiomeReference.unwrapKey().orElseThrow();
		var candidateFeatures = createFeaturesWithAddedFeature(
			getFeaturesForBiome(simulatedFeatures, targetBiomeReference),
			placedFeatureReference,
			generationStep
		);

		Map<ResourceKey<Biome>, List<HolderSet<PlacedFeature>>> candidateSimulatedFeatures = new HashMap<>(simulatedFeatures);
		candidateSimulatedFeatures.put(targetBiomeKey, candidateFeatures);

		try {
			FeatureSorter.buildFeaturesPerStep(
				List.of(targetBiomeReference),
				biomeReference -> getFeaturesForBiome(candidateSimulatedFeatures, biomeReference),
				true
			);
			return true;
		} catch (IllegalStateException ignored) {
			return false;
		}
	}

	public static List<HolderSet<PlacedFeature>> createFeaturesWithAddedFeature(
		List<HolderSet<PlacedFeature>> oldFeatures,
		Holder<PlacedFeature> placedFeatureReference,
		GenerationStep.Decoration generationStep
	) {
		List<HolderSet<PlacedFeature>> features = new ArrayList<>(oldFeatures);
		int stepIndex = generationStep.ordinal();

		while (features.size() <= stepIndex) {
			features.add(HolderSet.direct(List.of()));
		}

		List<Holder<PlacedFeature>> stepFeatures = new ArrayList<>(features.get(stepIndex).stream().toList());

		if (!containsFeature(features, placedFeatureReference)) {
			stepFeatures.add(placedFeatureReference);
		}

		features.set(stepIndex, HolderSet.direct(stepFeatures));
		return features;
	}

	private static List<HolderSet<PlacedFeature>> createFeaturesWithRemovedFeature(
		List<HolderSet<PlacedFeature>> features,
		Holder<PlacedFeature> placedFeatureReference,
		GenerationStep.Decoration generationStep
	) {
		var newFeatures = new ArrayList<>(features);
		int stepIndex = generationStep.ordinal();

		if (stepIndex >= newFeatures.size()) {
			return newFeatures;
		}

		var stepFeatures = new ArrayList<>(newFeatures.get(stepIndex).stream().toList());

		stepFeatures.removeIf(feature -> feature.unwrapKey().equals(placedFeatureReference.unwrapKey()));

		newFeatures.set(stepIndex, HolderSet.direct(stepFeatures));

		return newFeatures;
	}

	public static List<HolderSet<PlacedFeature>> getFeaturesForBiome(
		Map<ResourceKey<Biome>, List<HolderSet<PlacedFeature>>> simulatedFeatures,
		Holder<Biome> biomeReference
	) {
		var biomeKey = biomeReference.unwrapKey().orElseThrow();
		return simulatedFeatures.getOrDefault(biomeKey, biomeReference.value().getGenerationSettings().features());
	}

	public static boolean containsFeature(List<HolderSet<PlacedFeature>> features, Holder<PlacedFeature> targetFeature) {
		var targetFeatureKey = targetFeature.unwrapKey().orElse(null);

		if (targetFeatureKey == null) {
			return false;
		}

		for (var featureSet : features) {
			for (var feature : featureSet) {
				if (feature.is(targetFeatureKey)) {
					return true;
				}
			}
		}

		return false;
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

		for (int stepIndex = 0; stepIndex < biome.getGenerationSettings().features().size(); stepIndex++) {
			for (Holder<PlacedFeature> feature : biome.getGenerationSettings().features().get(stepIndex)) {
				if (feature.is(targetFeatureKey)) {
					return GenerationStep.Decoration.values()[stepIndex];
				}
			}
		}

		return null;
	}
}