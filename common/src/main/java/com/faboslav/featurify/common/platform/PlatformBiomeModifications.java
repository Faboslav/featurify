package com.faboslav.featurify.common.platform;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface PlatformBiomeModifications
{
	void addPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biome, GenerationStep.Decoration generationStep);

	void removePlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep);
}