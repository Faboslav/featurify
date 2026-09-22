package com.faboslav.featurify.neoforge.worldgen;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.platform.PlatformHooks;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.versions.VersionedId;
import com.faboslav.featurify.common.worldgen.WorldgenDataUpdater;
import com.faboslav.featurify.neoforge.platform.NeoForgeBiomeModifications;
import com.faboslav.featurify.neoforge.registry.FeaturifyBiomeModifiers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.ArrayList;
import java.util.List;

public final class PlacedFeaturesBiomeModifier implements BiomeModifier
{
	public static final MapCodec<? extends BiomeModifier> CODEC = MapCodec.unit(PlacedFeaturesBiomeModifier::new);

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

		for (var placedFeatureBiomeModification : NeoForgeBiomeModifications.PLACED_FEATURE_BIOME_MODIFICATIONS) {
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

				if (!WorldgenDataUpdater.containsFeature(currentFeatures, placedFeatureReference) && WorldgenDataUpdater.canSafelyAddFeature(biome, currentFeatures, placedFeatureReference, step)) {
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
	public MapCodec<? extends BiomeModifier> codec() {
		return FeaturifyBiomeModifiers.BIOME_MODIFIER.get();
	}
}
