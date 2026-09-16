package com.faboslav.featurify.forge.worldgen;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformHooks;
import com.faboslav.featurify.common.versions.VersionedId;
import com.faboslav.featurify.common.worldgen.WorldgenDataUpdater;
import com.faboslav.featurify.forge.platform.ForgeBiomeModifications;
import com.faboslav.featurify.forge.registry.FeaturifyBiomeModifiers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.ArrayList;
import java.util.List;

public final class PlacedFeaturesBiomeModifier implements BiomeModifier
{
	public static final Codec<? extends BiomeModifier> CODEC = Codec.unit(PlacedFeaturesBiomeModifier::new);

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.AFTER_EVERYTHING && PlatformHooks.PLATFORM_BIOME_MODIFICATIONS.shouldApplyFeaturifyBiomeModifiers()) {
			modifyPlacedFeatures(biome, builder);
		}
	}

	private void modifyPlacedFeatures(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		var currentBiomeKey = biome.unwrapKey().orElse(null);

		if (currentBiomeKey == null) {
			return;
		}

		for (var placedFeatureBiomeModification : ForgeBiomeModifications.PLACED_FEATURE_BIOME_MODIFICATIONS) {
			var placedFeatureReference = placedFeatureBiomeModification.placedFeatureReference();
			var placedFeatureId = VersionedId.getId(placedFeatureReference.unwrapKey().orElseThrow()).toString();
			var biomeKey = placedFeatureBiomeModification.biomeReference().unwrapKey().orElseThrow();
			var biomeId = VersionedId.getId(biomeKey).toString();

			if (!currentBiomeKey.equals(biomeKey)) {
				continue;
			}

			var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().get(placedFeatureId);

			if (placedFeatureData == null) {
				continue;
			}

			var step = placedFeatureBiomeModification.generationStep();
			var generationSettings = builder.getGenerationSettings();

			if (placedFeatureData.getRemovedBiomes().contains(biomeId)) {
				generationSettings.getFeatures(step).remove(placedFeatureReference);
			}

			if (placedFeatureData.getAdditionalBiomes().contains(biomeId)) {
				var currentFeatures = getAllFeatures(builder);

				if (
					!WorldgenDataUpdater.containsFeature(currentFeatures, placedFeatureReference)
						&& WorldgenDataUpdater.canSafelyAddFeature(biome, currentFeatures, placedFeatureReference, step)
				) {
					generationSettings.addFeature(step, placedFeatureReference);
				}
			}
		}
	}

	private static List<HolderSet<PlacedFeature>> getAllFeatures(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		List<HolderSet<PlacedFeature>> features = new ArrayList<>();

		for (var decoration : GenerationStep.Decoration.values()) {
			features.add(HolderSet.direct(new ArrayList<>(builder.getGenerationSettings().getFeatures(decoration))));
		}

		return features;
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return FeaturifyBiomeModifiers.BIOME_MODIFIER.get();
	}
}
