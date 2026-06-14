package com.faboslav.featurify.forge.platform;

import com.faboslav.featurify.common.platform.PlatformBiomeModifications;
import com.faboslav.featurify.common.worldgen.PlacedFeatureBiomeModification;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.HashSet;
import java.util.Set;

public final class ForgeBiomeModifications implements PlatformBiomeModifications
{
	public static final Set<PlacedFeatureBiomeModification> PLACED_FEATURES_ADD_BIOME_MODIFICATIONS = new HashSet<>();
	public static final Set<PlacedFeatureBiomeModification> PLACED_FEATURES_REMOVE_BIOME_MODIFICATIONS = new HashSet<>();

	@Override
	public void addPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		PLACED_FEATURES_ADD_BIOME_MODIFICATIONS.add(new PlacedFeatureBiomeModification(placedFeatureReference, biomeReference, generationStep));
	}

	public void removePlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		PLACED_FEATURES_REMOVE_BIOME_MODIFICATIONS.add(new PlacedFeatureBiomeModification(placedFeatureReference, biomeReference, generationStep));
	}
}