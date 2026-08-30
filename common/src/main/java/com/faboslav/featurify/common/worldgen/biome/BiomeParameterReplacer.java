package com.faboslav.featurify.common.worldgen.biome;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.mixin.biome.ClimateParameterPointAccessor;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.versions.VersionedId;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.ArrayList;
import java.util.List;

public final class BiomeParameterReplacer
{
	public static Climate.ParameterList<Holder<Biome>> createReplacementList(
		Climate.ParameterList<Holder<Biome>> originalParameters,
		Climate.ParameterList<Holder<Biome>> previousReplacementList
	) {
		// TODO maybe, do this as a proper compat layer instead
		// TerraBlender builds and stores its region trees on the replacement instance.
		// Once initialized, we need to keep returning it or region biomes won’t work.
		//? if terrablender {
		if (previousReplacementList instanceof terrablender.worldgen.IExtendedParameterList<?> extendedParameterList && extendedParameterList.isInitialized()) {
			return previousReplacementList;
		}
		//?}

		List<Pair<Climate.ParameterPoint, Holder<Biome>>> originalEntries = originalParameters.values();
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> replacedEntries = null;
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> enabledEntries = null;

		for (int i = 0; i < originalEntries.size(); i++) {
			var entry = originalEntries.get(i);
			var biomeData = getBiomeData(entry.getSecond());

			if (shouldKeepOriginalBiome(biomeData)) {
				if (replacedEntries != null) {
					replacedEntries.add(entry);
				}

				continue;
			}

			if (replacedEntries == null) {
				replacedEntries = new ArrayList<>(originalEntries.size());
				replacedEntries.addAll(originalEntries.subList(0, i));
			}

			Holder<Biome> replacementBiome;

			if (biomeData.isUsingDefaultReplacementBiome()) {
				if (enabledEntries == null) {
					enabledEntries = getEnabledEntries(originalEntries);
				}

				replacementBiome = findClosestEnabledBiome(entry.getFirst(), enabledEntries);
			} else {
				replacementBiome = getConfiguredReplacementBiome(biomeData.getReplacementBiome());
			}

			replacedEntries.add(Pair.of(
				entry.getFirst(),
				replacementBiome
			));
		}

		if(replacedEntries == null) {
			return originalParameters;
		}

		return new Climate.ParameterList<>(replacedEntries);
	}

	private static List<Pair<Climate.ParameterPoint, Holder<Biome>>> getEnabledEntries(
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> entries
	) {
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> enabledEntries = new ArrayList<>(entries.size());

		for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : entries) {
			if (shouldKeepOriginalBiome(getBiomeData(entry.getSecond()))) {
				enabledEntries.add(entry);
			}
		}

		return enabledEntries;
	}

	private static BiomeData getBiomeData(Holder<Biome> biome) {
		String biomeId = VersionedId.GetId(biome.unwrapKey().get()).toString();

		return Featurify.getConfig().getBiomeData().get(biomeId);
	}

	private static boolean shouldKeepOriginalBiome(BiomeData biomeData) {
		return biomeData == null || biomeData.isUsingDefaultValues();
	}

	private static Holder<Biome> getConfiguredReplacementBiome(String replacementBiomeId) {
		ResourceKey<Biome> replacementBiomeKey = ResourceKey.create(
			Registries.BIOME,
			Featurify.makeNamespacedId(replacementBiomeId.replace("#", ""))
		);

		return RegistryManagerProvider
			.getBiomeRegistry()
			.get(replacementBiomeKey)
			.orElseThrow(() -> new IllegalArgumentException(
				"Unknown replacement biome: " + replacementBiomeId
			));
	}

	private static Holder<Biome> findClosestEnabledBiome(
		Climate.ParameterPoint disabledPoint,
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> enabledEntries
	) {
		if (enabledEntries.isEmpty()) {
			// TODO no exception
			throw new IllegalStateException(
				"Cannot automatically replace a biome because the biome source contains no enabled biomes"
			);
		}

		Climate.TargetPoint targetPoint = createCenterTarget(disabledPoint);

		//? if >= 1.21.1 {
		Holder<Biome> closestBiome = enabledEntries.getFirst().getSecond();
		//?} else {
		/*Holder<Biome> closestBiome = enabledEntries.get(0).getSecond();
		*///?}
		long closestFitness = Long.MAX_VALUE;

		for (Pair<Climate.ParameterPoint, Holder<Biome>> enabledEntry : enabledEntries) {
			long fitness = ((ClimateParameterPointAccessor) (Object) enabledEntry.getFirst()).featurify$fitness(targetPoint);

			if (fitness < closestFitness) {
				closestFitness = fitness;
				closestBiome = enabledEntry.getSecond();
			}
		}

		return closestBiome;
	}

	private static Climate.TargetPoint createCenterTarget(
		Climate.ParameterPoint point
	) {
		return new Climate.TargetPoint(
			center(point.temperature()),
			center(point.humidity()),
			center(point.continentalness()),
			center(point.erosion()),
			center(point.depth()),
			center(point.weirdness())
		);
	}

	private static long center(Climate.Parameter parameter) {
		return parameter.min() + (parameter.max() - parameter.min()) / 2L;
	}
}