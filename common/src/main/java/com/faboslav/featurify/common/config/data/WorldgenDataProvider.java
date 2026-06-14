package com.faboslav.featurify.common.config.data;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

		for (var biomeTag : biomeRegistry.listTags().toList()) {
			biomes.add('#' + biomeTag.unwrapKey().get().location().toString());
		}

		for (var biome : biomeRegistry.listElements().toList()) {
			biomes.add(biome.unwrapKey().get()/*? if >= 1.21.11 {*//*.identifier()*//*?} else {*/.location()/*?}*/.toString());
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
			var placedFeature = placedFeatureReference.value();
			String placedFeatureId = placedFeatureReference.key()/*? if >= 1.21.11 {*//*.identifier()*//*?} else {*/.location()/*?}*/.toString();

			var defaultBiomes = new ArrayList<String>();

			for (var biomeReference : biomeRegistry.listElements().toList()) {
				if (getFeatureStep(biomeReference.value(), placedFeatureReference) == null) {
					continue;
				}

				String biomeId = biomeReference.key()/*? if >= 1.21.11 {*//*.identifier()*//*?} else {*/.location()/*?}*/.toString();

				if (!defaultBiomes.contains(biomeId)) {
					defaultBiomes.add(biomeId);
				}
			}

			PlacedFeatureData placedFeatureData = new PlacedFeatureData(defaultBiomes);
			placedFeatures.put(placedFeatureId, placedFeatureData);
		}

		return placedFeatures;
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