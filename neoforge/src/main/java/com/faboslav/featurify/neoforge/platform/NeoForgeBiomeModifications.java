package com.faboslav.featurify.neoforge.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformBiomeModifications;
import com.faboslav.featurify.common.worldgen.PlacedFeatureBiomeModification;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public final class NeoForgeBiomeModifications implements PlatformBiomeModifications
{
	public static final Set<PlacedFeatureBiomeModification> PLACED_FEATURE_BIOME_MODIFICATIONS = new HashSet<>();
	private static final ResourceKey<BiomeModifier> FABRIC_BIOME_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Featurify.makeId("fabric_biome_api_v1", "fabric_biome_modifier_instance"));
	private boolean shouldApplyFeaturifyBiomeModifiers = true;

	@Override
	public void modifyPlacedFeature(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep) {
		PLACED_FEATURE_BIOME_MODIFICATIONS.add(new PlacedFeatureBiomeModification(placedFeatureReference, biomeReference, generationStep));
	}

	@Override
	public void applyBiomeModifiers(RegistryAccess registryAccess) {
		var biomeModifierRegistry = registryAccess.lookup(NeoForgeRegistries.Keys.BIOME_MODIFIERS).orElse(null);
		var biomeRegistry = registryAccess.lookup(Registries.BIOME).orElse(null);

		if (biomeModifierRegistry == null || biomeRegistry == null) {
			return;
		}

		var biomeModifiers = biomeModifierRegistry.listElements()
			.filter(biomeModifier -> !biomeModifier.is(FABRIC_BIOME_MODIFIER))
			.map(Holder::value)
			.toList();

		for (Holder.Reference<Biome> biomeHolder : biomeRegistry.listElements().toList()) {
			biomeHolder.value().modifiableBiomeInfo().applyBiomeModifiers(biomeHolder, biomeModifiers, registryAccess);
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