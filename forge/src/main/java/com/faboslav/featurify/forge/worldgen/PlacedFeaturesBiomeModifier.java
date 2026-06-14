package com.faboslav.featurify.forge.worldgen;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.forge.platform.ForgeBiomeModifications;
import com.faboslav.featurify.forge.registry.FeaturifyBiomeModifiers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public final class PlacedFeaturesBiomeModifier implements BiomeModifier
{
	public static final Codec<? extends BiomeModifier> CODEC = Codec.unit(PlacedFeaturesBiomeModifier::new);

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD) {
			AddPlacedFeature(biome, builder);
		} else if(phase == Phase.REMOVE) {
			RemovePlacedFeature(biome, builder);
		}
	}

	private void AddPlacedFeature(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		var currentBiomeKey = biome.unwrapKey().orElse(null);

		if (currentBiomeKey == null) {
			return;
		}

		for(var placedFeatureBiomeModification : ForgeBiomeModifications.PLACED_FEATURES_ADD_BIOME_MODIFICATIONS) {
			var placedFeatureKey = placedFeatureBiomeModification.placedFeatureReference().unwrapKey().orElseThrow();
			var placedFeatureId = placedFeatureKey.location().toString();
			var biomeKey = placedFeatureBiomeModification.biomeReference().unwrapKey().orElseThrow();

			if (!currentBiomeKey.equals(biomeKey)) {
				continue;
			}

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

			if(placedFeatureData == null || !placedFeatureData.getAdditionalBiomes().contains(biomeKey.location().toString())) {
				continue;
			}

			builder.getGenerationSettings().addFeature(placedFeatureBiomeModification.generationStep(), placedFeatureBiomeModification.placedFeatureReference());
		}
	}


	private void RemovePlacedFeature(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		var currentBiomeKey = biome.unwrapKey().orElse(null);

		if (currentBiomeKey == null) {
			return;
		}

		for(var placedFeatureBiomeModification : ForgeBiomeModifications.PLACED_FEATURES_ADD_BIOME_MODIFICATIONS) {
			var placedFeatureKey = placedFeatureBiomeModification.placedFeatureReference().unwrapKey().orElseThrow();
			var placedFeatureId = placedFeatureKey.location().toString();
			var biomeKey = placedFeatureBiomeModification.biomeReference().unwrapKey().orElseThrow();

			if (!currentBiomeKey.equals(biomeKey)) {
				continue;
			}

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

			if(placedFeatureData == null || !placedFeatureData.getRemovedBiomes().contains(biomeKey.location().toString())) {
				continue;
			}

			builder.getGenerationSettings().getFeatures(placedFeatureBiomeModification.generationStep()).remove(placedFeatureBiomeModification.placedFeatureReference());
		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return FeaturifyBiomeModifiers.BIOME_MODIFIER.get();
	}
}