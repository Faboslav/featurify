package com.faboslav.featurify.common.util;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.*;
import java.util.stream.Collectors;

public final class BiomeUtil
{
	public static TagKey<Biome> C_IS_OCEAN = TagKey.create(Registries.BIOME, Featurify.makeNamespacedId("c:is_ocean"));

	public static Set<ResourceKey<Biome>> getOceanBiomes() {
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		if (biomeRegistry == null) {
			return new HashSet<>();
		}

		Set<ResourceKey<Biome>> oceanBiomes = new HashSet<>();

		biomeRegistry.get(BiomeTags.IS_OCEAN).ifPresent(named ->
			oceanBiomes.addAll(named.stream()
				.map(h -> h.unwrapKey().orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()))
		);

		biomeRegistry.get(BiomeTags.IS_RIVER).ifPresent(named ->
			oceanBiomes.addAll(named.stream()
				.map(h -> h.unwrapKey().orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()))
		);

		biomeRegistry.get(C_IS_OCEAN).ifPresent(named ->
			oceanBiomes.addAll(named.stream()
				.map(h -> h.unwrapKey().orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet()))
		);

		return oceanBiomes;
	}
}
