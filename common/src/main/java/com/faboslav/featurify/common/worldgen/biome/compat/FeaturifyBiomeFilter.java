package com.faboslav.featurify.common.worldgen.biome.compat;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.versions.VersionedId;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

//? if >= 26.3 {
import net.minecraft.world.level.biome.BiomeSource;
//?} else {
//import net.minecraft.world.level.biome.BiomeResolver;
//?}

import java.util.function.Supplier;

public final class FeaturifyBiomeFilter
{
	public static ResourceKey<Biome> getReplacementBiomeKey(ResourceKey<Biome> biomeKey) {
		String biomeId = VersionedId.getId(biomeKey).toString();

		if (MarkerBiomes.isMarkerBiome(biomeId)) {
			return biomeKey;
		}

		BiomeData biomeData = Featurify.getConfig().getBiomeData().get(biomeId);

		if (shouldKeepOriginalBiome(biomeData)) {
			return biomeKey;
		}

		if (!biomeData.isUsingDefaultReplacementBiome()) {
			var replacementBiomeId = biomeData.getReplacementBiome().replace("#", "");

			if (MarkerBiomes.isMarkerBiome(replacementBiomeId)) {
				Featurify.getLogger().warn("Replacement biome \"{}\" for \"{}\" is a marker biome and cannot be used as a replacement, keeping original biome.", replacementBiomeId, biomeId);
				return biomeKey;
			}

			return ResourceKey.create(
				Registries.BIOME,
				Featurify.makeNamespacedId(replacementBiomeId)
			);
		}

		return null;
	}

	public static Holder<Biome> getReplacementBiome(
		Holder<Biome> biome,
		int quartX,
		int quartY,
		int quartZ,
		Climate.Sampler sampler,
		//? if >= 26.3 {
		BiomeSource baseResolver
		//?} else {
		//BiomeResolver baseResolver
		//?}
	) {
		//? if >= 26.3 {
		return getReplacementBiome(biome, () -> baseResolver.createResolver(sampler).getNoiseBiome(quartX, quartY, quartZ));
		//?} else {
		//return getReplacementBiome(biome, () -> baseResolver.getNoiseBiome(quartX, quartY, quartZ, sampler));
		//?}
	}

	public static Holder<Biome> getReplacementBiome(
		Holder<Biome> biome,
		Supplier<Holder<Biome>> baseBiomeSupplier
	) {
		var biomeKey = biome.unwrapKey().orElse(null);

		if (biomeKey == null) {
			return biome;
		}

		String biomeId = VersionedId.getId(biomeKey).toString();

		if (MarkerBiomes.isMarkerBiome(biomeId)) {
			return biome;
		}

		BiomeData biomeData = Featurify.getConfig().getBiomeData().get(biomeId);

		if (shouldKeepOriginalBiome(biomeData)) {
			return biome;
		}

		if (!biomeData.isUsingDefaultReplacementBiome()) {
			var replacementBiomeId = biomeData.getReplacementBiome().replace("#", "");

			if (MarkerBiomes.isMarkerBiome(replacementBiomeId)) {
				Featurify.getLogger().warn("Replacement biome \"{}\" for \"{}\" is a marker biome and cannot be used as a replacement, keeping original biome.", replacementBiomeId, biomeId);
				return biome;
			}

			return getConfiguredReplacementBiome(replacementBiomeId);
		}

		return baseBiomeSupplier.get();
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
}
