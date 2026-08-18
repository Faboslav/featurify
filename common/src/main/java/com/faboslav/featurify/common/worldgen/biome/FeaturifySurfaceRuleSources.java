package com.faboslav.featurify.common.worldgen.biome;

import com.faboslav.featurify.common.worldgen.WorldgenDataProvider;
import com.faboslav.featurify.common.versions.VersionedId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//? if >= 26.2 {
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
//?}

public final class FeaturifySurfaceRuleSources
{
	public static SurfaceRules.RuleSource createReplacementRules() {
		Map<String, Map<String, SurfaceRules.RuleSource>> surfaceRulesByDimension =
			WorldgenDataProvider.getSurfaceRuleSources();

		if (surfaceRulesByDimension.isEmpty()) {
			return null;
		}

		List<SurfaceRules.RuleSource> replacementRules = new ArrayList<>();

		for (ResourceKey<Biome> biome : BiomeReplacementData.getReplacementBiomes()) {
			SurfaceRules.RuleSource sourceRule =
				findUniqueSurfaceRule(surfaceRulesByDimension, biome);

			if (sourceRule != null) {
				replacementRules.add(createBiomeRule(biome, sourceRule));
			}
		}

		if(replacementRules.isEmpty()) {
			return null;
		}

		return SurfaceRules.sequence(replacementRules.toArray(SurfaceRules.RuleSource[]::new));
	}

	private static SurfaceRules.RuleSource findUniqueSurfaceRule(
		Map<String, Map<String, SurfaceRules.RuleSource>> surfaceRulesByDimension,
		ResourceKey<Biome> biome
	) {
		String biomeId = VersionedId.GetId(biome).toString();
		SurfaceRules.RuleSource foundRuleSource = null;

		for (var dimensionRules : surfaceRulesByDimension.values()) {
			SurfaceRules.RuleSource possibleRuleSource = dimensionRules.get(biomeId);

			if (possibleRuleSource == null) {
				continue;
			}

			if (foundRuleSource != null) {
				return null;
			}

			foundRuleSource = possibleRuleSource;
		}

		return foundRuleSource;
	}

	private static SurfaceRules.RuleSource createBiomeRule(
		ResourceKey<Biome> biome,
		SurfaceRules.RuleSource sourceRule
	) {
		return SurfaceRules.ifTrue(
			//? >= 26.2 {
			SurfaceRules.isBiome(RegistryManagerProvider.getBiomeRegistry(), biome),
			//?} else {
			/*SurfaceRules.isBiome(biome),
			*///?}
			sourceRule
		);
	}
}