package com.faboslav.featurify.fabric.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformBiomeModifications;
import com.faboslav.featurify.common.versions.VersionedId;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.HashSet;
import java.util.Set;

public final class FabricBiomeModifications implements PlatformBiomeModifications
{
	private static final Set<ResourceLocation> ADDED_MODIFICATIONS = new HashSet<>();
	private static final Set<ResourceLocation> REMOVED_MODIFICATIONS = new HashSet<>();

	@Override
	public void addPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		var placedFeatureKey = placedFeatureReference.unwrapKey().orElseThrow();
		var placedFeatureId = VersionedId.GetId(placedFeatureKey).toString();
		var biomeKey = biomeReference.unwrapKey().orElseThrow();
		var biomeModificationId = "add_" + VersionedId.GetId(biomeKey).getNamespace() + "_" + VersionedId.GetId(biomeKey).getPath() + "_" + VersionedId.GetId(placedFeatureKey).getNamespace() + "_" + VersionedId.GetId(placedFeatureKey).getPath();
		var modificationId = Featurify.makeId(biomeModificationId.replace('/', '_'));

		if (!ADDED_MODIFICATIONS.add(modificationId)) {
			return;
		}

		BiomeModifications.create(modificationId)
			.add(
				ModificationPhase.ADDITIONS,
				context -> context.getBiomeKey().equals(biomeKey),
				context -> {
					var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

					if(placedFeatureData == null || !placedFeatureData.getAdditionalBiomes().contains(VersionedId.GetId(biomeKey).toString())) {
						return;
					}


					Featurify.getLogger().info("Adding: " + biomeModificationId);
					context.getGenerationSettings().addFeature(generationStep, placedFeatureKey);
				}
			);
	}

	public void removePlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		var placedFeatureKey = placedFeatureReference.unwrapKey().orElseThrow();
		var placedFeatureId = VersionedId.GetId(placedFeatureKey).toString();
		var biomeKey = biomeReference.unwrapKey().orElseThrow();
		var biomeModificationId = "remove_" + VersionedId.GetId(biomeKey).getNamespace() + "_" + VersionedId.GetId(biomeKey).getPath() + "_" + VersionedId.GetId(placedFeatureKey).getNamespace() + "_" + VersionedId.GetId(placedFeatureKey).getPath();
		var modificationId = Featurify.makeId(biomeModificationId.replace('/', '_'));

		if (!REMOVED_MODIFICATIONS.add(modificationId)) {
			return;
		}

		BiomeModifications.create(modificationId)
			.add(
				ModificationPhase.REMOVALS,
				context -> true,
				context -> {
					var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().getOrDefault(placedFeatureId, null);

					if(placedFeatureData == null || !placedFeatureData.getRemovedBiomes().contains(VersionedId.GetId(biomeKey).toString())) {
						return;
					}

					context.getGenerationSettings().removeFeature(generationStep, placedFeatureKey);
				}
			);
	}
}