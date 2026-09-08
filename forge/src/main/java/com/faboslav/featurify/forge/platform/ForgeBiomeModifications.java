package com.faboslav.featurify.forge.platform;

import com.faboslav.featurify.common.platform.PlatformBiomeModifications;
import com.faboslav.featurify.common.worldgen.PlacedFeatureBiomeModification;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public final class ForgeBiomeModifications implements PlatformBiomeModifications
{
	public static final Set<PlacedFeatureBiomeModification> PLACED_FEATURES_ADD_BIOME_MODIFICATIONS = new HashSet<>();
	public static final Set<PlacedFeatureBiomeModification> PLACED_FEATURES_REMOVE_BIOME_MODIFICATIONS = new HashSet<>();
	private boolean shouldApplyFeaturifyBiomeModifiers = true;

	@Override
	public void addPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		PLACED_FEATURES_ADD_BIOME_MODIFICATIONS.add(new PlacedFeatureBiomeModification(placedFeatureReference, biomeReference, generationStep));
	}

	public void removePlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		PLACED_FEATURES_REMOVE_BIOME_MODIFICATIONS.add(new PlacedFeatureBiomeModification(placedFeatureReference, biomeReference, generationStep));
	}

	@Override
	public void applyBiomeModifiers(RegistryAccess registryAccess) {
		var biomeModifierRegistry = registryAccess.lookup(ForgeRegistries.Keys.BIOME_MODIFIERS).orElse(null);
		var biomeRegistry = registryAccess.lookup(Registries.BIOME).orElse(null);

		if (biomeModifierRegistry == null || biomeRegistry == null) {
			return;
		}

		var biomeModifiers = biomeModifierRegistry.listElements().map(Holder::value).toList();

		for (Holder.Reference<Biome> biomeHolder : biomeRegistry.listElements().toList()) {
			biomeHolder.value().modifiableBiomeInfo().applyBiomeModifiers(biomeHolder, biomeModifiers);
		}
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