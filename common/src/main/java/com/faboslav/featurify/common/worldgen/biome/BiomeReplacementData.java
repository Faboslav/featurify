package com.faboslav.featurify.common.worldgen.biome;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BiomeReplacementData
{
	private static final Set<ResourceKey<Biome>> replacementBiomes = new HashSet<>();

	public static void addReplacementBiome(String biomeId) {
		var replacementBiomeKey = ResourceKey.create(Registries.BIOME, Featurify.makeNamespacedId(biomeId.replace("#", "")));
		var replacementBiomeHolder = RegistryManagerProvider.getBiomeRegistry().get(replacementBiomeKey).orElse(null);

		if(replacementBiomeHolder == null) {
			return;
		}

		replacementBiomes.add(replacementBiomeKey);
	}

	public static Set<ResourceKey<Biome>> getReplacementBiomes() {
		return Collections.unmodifiableSet(replacementBiomes);
	}

	public static void clearReplacementBiomes() {
		replacementBiomes.clear();
	}
}
