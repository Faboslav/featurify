package com.faboslav.featurify.common.worldgen.biome.compat;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.versions.VersionedId;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;

public final class LithostitchedBiomeFilter
{
	public static Holder<Biome> filter(
		Holder<Biome> biome,
		int quartX,
		int quartY,
		int quartZ,
		Climate.Sampler sampler,
		BiomeResolver baseResolver
	) {
		var biomeKey = biome.unwrapKey().orElse(null);

		if (biomeKey == null) {
			return biome;
		}

		String biomeId = VersionedId.GetId(biomeKey).toString();
		BiomeData biomeData = Featurify.getConfig().getBiomeData().get(biomeId);

		if (biomeData == null || biomeData.isUsingDefaultValues()) {
			return biome;
		}

		if (!biomeData.isUsingDefaultReplacementBiome()) {
			return getConfiguredReplacementBiome(biomeData.getReplacementBiome());
		}

		return baseResolver.getNoiseBiome(quartX, quartY, quartZ, sampler);
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
