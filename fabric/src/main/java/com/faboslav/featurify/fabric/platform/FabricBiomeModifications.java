package com.faboslav.featurify.fabric.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformBiomeModifications;
import com.faboslav.featurify.common.versions.VersionedId;
import com.faboslav.featurify.common.worldgen.WorldgenDataUpdater;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.impl.biome.modification.BiomeModificationImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.HashSet;
import java.util.Set;

public final class FabricBiomeModifications implements PlatformBiomeModifications
{
	private static final Set<Identifier> REGISTERED_MODIFICATIONS = new HashSet<>();
	private boolean shouldApplyFeaturifyBiomeModifiers = true;

	@Override
	public void modifyPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		var placedFeatureKey = placedFeatureReference.unwrapKey().orElseThrow();
		var placedFeatureId = VersionedId.getId(placedFeatureKey);
		var stringPlacedFeatureId = placedFeatureId.toString();
		var biomeKey = biomeReference.unwrapKey().orElseThrow();
		var biomeId = VersionedId.getId(biomeKey);
		var stringBiomeId = biomeId.toString();
		var modificationId = Featurify.makeId((biomeId.getNamespace() + "_" + biomeId.getPath() + "_" + placedFeatureId.getNamespace() + "_" + placedFeatureId.getPath()).replace('/', '_'));

		if (!REGISTERED_MODIFICATIONS.add(modificationId)) {
			return;
		}

		BiomeModifications.create(modificationId)
			.add(
				ModificationPhase.POST_PROCESSING,
				context -> context.getBiomeKey().equals(biomeKey),
				(selectionContext, modificationContext) -> {
					if (!this.shouldApplyFeaturifyBiomeModifiers()) {
						return;
					}

					var placedFeatureData = Featurify.getConfig().getPlacedFeatureData().get(stringPlacedFeatureId);

					if (placedFeatureData == null) {
						return;
					}

					if (placedFeatureData.getRemovedBiomes().contains(stringBiomeId)) {
						modificationContext.getGenerationSettings().removeFeature(generationStep, placedFeatureKey);
					}

					if (!placedFeatureData.getAdditionalBiomes().contains(stringBiomeId)) {
						return;
					}

					var currentFeatures = selectionContext.getBiome().getGenerationSettings().features();

					if (WorldgenDataUpdater.containsFeature(currentFeatures, placedFeatureReference)) {
						return;
					}

					if (!WorldgenDataUpdater.canSafelyAddFeature(selectionContext.getBiomeRegistryEntry(), currentFeatures, placedFeatureReference, generationStep)) {
						return;
					}

					modificationContext.getGenerationSettings().addFeature(generationStep, placedFeatureKey);
				}
			);
	}

	@Override
	public void applyBiomeModifiers(RegistryAccess registryAccess) {
		BiomeModificationImpl.INSTANCE.finalizeWorldGen(registryAccess);
	}

	@Override
	public boolean shouldApplyFeaturifyBiomeModifiers() {
		return shouldApplyFeaturifyBiomeModifiers;
	}

	@Override
	public void setShouldApplyFeaturifyBiomeModifiers(boolean shouldApplyBiomeModifiers) {
		shouldApplyFeaturifyBiomeModifiers = shouldApplyBiomeModifiers;
	}
}
