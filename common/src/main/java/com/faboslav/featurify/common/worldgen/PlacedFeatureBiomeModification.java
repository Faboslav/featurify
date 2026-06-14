package com.faboslav.featurify.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Objects;

public record PlacedFeatureBiomeModification(Holder<PlacedFeature> placedFeatureReference, Holder<Biome> biomeReference, GenerationStep.Decoration generationStep)
{
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PlacedFeatureBiomeModification other)) {
			return false;
		}

		return this.generationStep == other.generationStep
			   && this.placedFeatureReference.unwrapKey().orElseThrow().equals(other.placedFeatureReference.unwrapKey().orElseThrow())
			   && this.biomeReference.unwrapKey().orElseThrow().equals(other.biomeReference.unwrapKey().orElseThrow());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			this.placedFeatureReference.unwrapKey().orElseThrow(),
			this.biomeReference.unwrapKey().orElseThrow(),
			this.generationStep
		);
	}
}
