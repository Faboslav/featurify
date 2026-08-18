package com.faboslav.featurify.common.worldgen;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.config.data.PlacedFeatureData;
import com.faboslav.featurify.common.mixin.biome.MultiNoiseBiomeSourceAccessor;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import com.faboslav.featurify.common.util.FeatureUtil;
import com.faboslav.featurify.common.versions.VersionedId;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.*;

public final class WorldgenDataProvider
{
	private static Set<String> biomeIds = new HashSet<>();
	private static Set<String> biomeTags = new HashSet<>();
	private static Map<String, Map<String, SurfaceRules.RuleSource>> surfaceRuleSources = new TreeMap<>();
	private static Map<String, BiomeData> biomes = new TreeMap<>();
	private static Map<String, PlacedFeatureData> placedFeatures = new TreeMap<>();

	public static Set<String> getBiomeIds() {
		return biomeIds;
	}

	public static Set<String> getBiomeTags() {
		return biomeTags;
	}

	public static Map<String, Map<String, SurfaceRules.RuleSource>> getSurfaceRuleSources() {
		return surfaceRuleSources;
	}

	public static Map<String, BiomeData> getBiomes() {
		return biomes;
	}

	public static Map<String, PlacedFeatureData> getPlacedFeatures() {
		return placedFeatures;
	}

	public static void loadWorldgenData() {
		biomeIds = loadBiomeIds();
		biomeTags = loadBiomeTags();
		biomes = loadBiomes();
		placedFeatures = loadPlacedFeatures();
	}

	public static Set<String> loadBiomeIds() {
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return Collections.emptySet();
		}

		Set<String> biomeIds = new HashSet<>();

		for (var biome : biomeRegistry.listElements().toList()) {
			biomeIds.add(VersionedId.GetId(biome.unwrapKey().orElseThrow()).toString());
		}

		return biomeIds;
	}

	public static Set<String> loadBiomeTags() {
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return Collections.emptySet();
		}

		Set<String> biomeTags = new HashSet<>();

		for (var biomeTag : biomeRegistry.listTags().toList()) {
			biomeTags.add('#' + biomeTag.unwrapKey().get().location().toString());
		}

		return biomeTags;
	}

	public static Map<String, Map<String, SurfaceRules.RuleSource>> loadSurfaceRuleSources() {
		Map<String, Map<String, SurfaceRules.RuleSource>> surfaceRuleSources = new TreeMap<>(Comparators.ALPHABETICALL_ID_COMPARATOR);

		var levelStemRegistry = RegistryManagerProvider.getLevelStemRegistry();

		if (levelStemRegistry == null) {
			return Collections.emptyMap();
		}

		for (var levelStem : levelStemRegistry.listElements().toList()) {
			String dimensionId = VersionedId.GetId(levelStem.key()).toString();

			ChunkGenerator chunkGenerator = levelStem.value().generator();

			if (!(chunkGenerator instanceof NoiseBasedChunkGenerator noiseGenerator)) {
				continue;
			}

			SurfaceRules.RuleSource surfaceRule =
				noiseGenerator.generatorSettings().value().surfaceRule();

			Set<ResourceKey<Biome>> biomeKeys =
				collectBiomeKeys(dimensionId, noiseGenerator.getBiomeSource());

			if (biomeKeys.isEmpty()) {
				continue;
			}

			Map<String, SurfaceRules.RuleSource> dimensionSurfaceRuleSources =
				new TreeMap<>(Comparators.ALPHABETICALL_ID_COMPARATOR);

			for (ResourceKey<Biome> biomeKey : biomeKeys) {
				dimensionSurfaceRuleSources.put(
					VersionedId.GetId(biomeKey).toString(),
					surfaceRule
				);
			}

			surfaceRuleSources.put(
				dimensionId,
				dimensionSurfaceRuleSources
			);
		}

		WorldgenDataProvider.surfaceRuleSources = surfaceRuleSources;

		return surfaceRuleSources;
	}

	private static Set<ResourceKey<Biome>> collectBiomeKeys(
		String dimensionId,
		BiomeSource biomeSource
	) {
		Set<ResourceKey<Biome>> biomeKeys = new HashSet<>();

		if (biomeSource instanceof MultiNoiseBiomeSource multiNoiseBiomeSource) {
			try {
				var parameters = ((MultiNoiseBiomeSourceAccessor) multiNoiseBiomeSource).featurify$getParameters();
				Climate.ParameterList<Holder<Biome>> parameterList = parameters.map(direct -> direct, holder -> holder.value().parameters());

				for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : parameterList.values()) {
					entry.getSecond().unwrapKey().ifPresent(biomeKeys::add);
				}

				return biomeKeys;
			} catch (Throwable throwable) {
				Featurify.getLogger().warn(
					"Failed to read MultiNoiseBiomeSource parameters for {}. Falling back to possibleBiomes().",
					dimensionId,
					throwable
				);
			}
		}

		try {
			for (Holder<Biome> biome : biomeSource.possibleBiomes()) {
				biome.unwrapKey().ifPresent(biomeKeys::add);
			}
		} catch (Throwable throwable) {
			Featurify.getLogger().error(
				"Failed to enumerate possible biomes for {} ({})",
				dimensionId,
				biomeSource.getClass().getName(),
				throwable
			);
		}

		return biomeKeys;
	}

	public static Map<String, BiomeData> loadBiomes() {
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return Collections.emptyMap();
		}

		Map<String, BiomeData> biomes = new TreeMap<>(Comparators.ALPHABETICALL_ID_COMPARATOR);

		for (var biome : biomeRegistry.listElements().toList()) {
			var biomeId = VersionedId.GetId(biome.unwrapKey().orElseThrow()).toString();
			BiomeData biomeData = new BiomeData();
			biomes.put(biomeId.toString(), biomeData);
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