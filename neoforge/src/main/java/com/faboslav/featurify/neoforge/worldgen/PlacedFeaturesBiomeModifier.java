package com.faboslav.featurify.neoforge.worldgen;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.neoforge.platform.NeoForgeBiomeModifications;
import com.faboslav.featurify.neoforge.registry.FeaturifyBiomeModifiers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public final class PlacedFeaturesBiomeModifier implements BiomeModifier
{
	public static final MapCodec<? extends BiomeModifier> CODEC = MapCodec.unit(PlacedFeaturesBiomeModifier::new);

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

		for(var placedFeatureBiomeModification : NeoForgeBiomeModifications.PLACED_FEATURES_ADD_BIOME_MODIFICATIONS) {
			var placedFeatureKey = placedFeatureBiomeModification.placedFeatureReference().unwrapKey().orElseThrow();
			var placedFeatureId = placedFeatureKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString();
			var biomeKey = placedFeatureBiomeModification.biomeReference().unwrapKey().orElseThrow();

			if (!currentBiomeKey.equals(biomeKey)) {
				continue;
			}

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

			if(placedFeatureData == null || !placedFeatureData.getAdditionalBiomes().contains(biomeKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString())) {
				continue;
			}

			Featurify.getLogger().info("AddPlacedFeature");
			builder.getGenerationSettings().addFeature(placedFeatureBiomeModification.generationStep(), placedFeatureBiomeModification.placedFeatureReference());
		}
	}


	private void RemovePlacedFeature(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		var currentBiomeKey = biome.unwrapKey().orElse(null);

		if (currentBiomeKey == null) {
			return;
		}

		for(var placedFeatureBiomeModification : NeoForgeBiomeModifications.PLACED_FEATURES_REMOVE_BIOME_MODIFICATIONS) {
			var placedFeatureKey = placedFeatureBiomeModification.placedFeatureReference().unwrapKey().orElseThrow();
			var placedFeatureId = placedFeatureKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString();
			var biomeKey = placedFeatureBiomeModification.biomeReference().unwrapKey().orElseThrow();

			if (!currentBiomeKey.equals(biomeKey)) {
				continue;
			}

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

			if(placedFeatureData == null || !placedFeatureData.getRemovedBiomes().contains(biomeKey/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toString())) {
				continue;
			}

			Featurify.getLogger().info("RemovePlacedFeature");
			builder.getGenerationSettings().getFeatures(placedFeatureBiomeModification.generationStep()).remove(placedFeatureBiomeModification.placedFeatureReference());
		}
	}

	@Override
	public MapCodec<? extends BiomeModifier> codec() {
		return FeaturifyBiomeModifiers.BIOME_MODIFIER.get();
	}
}