package com.faboslav.featurify.common.worldgen.biome.compat;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.versions.VersionedId;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class TerraBlenderBiomeFilter
{
	public static ResourceKey<Biome> getReplacementBiomeKey(ResourceKey<Biome> biomeKey) {
		String biomeId = VersionedId.GetId(biomeKey).toString();
		BiomeData biomeData = Featurify.getConfig().getBiomeData().get(biomeId);

		if (shouldKeepOriginalBiome(biomeData)) {
			return biomeKey;
		}

		if (!biomeData.isUsingDefaultReplacementBiome()) {
			return ResourceKey.create(
				Registries.BIOME,
				Featurify.makeNamespacedId(biomeData.getReplacementBiome().replace("#", ""))
			);
		}

		return null;
	}

	private static boolean shouldKeepOriginalBiome(BiomeData biomeData) {
		return biomeData == null
			   || (
				   biomeData.isUsingDefaultIsDisabled()
				   && biomeData.isUsingDefaultReplacementBiome()
			   );
	}
}
