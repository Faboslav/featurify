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

		List<SurfaceRules.RuleSource> rules = new ArrayList<>();

		for (ResourceKey<Biome> biome : BiomeReplacementData.getReplacementBiomes()) {
			SurfaceRules.RuleSource sourceRule =
				findUniqueSurfaceRule(surfaceRulesByDimension, biome);

			if (sourceRule != null) {
				rules.add(createBiomeRule(biome, sourceRule));
			}
		}

		return rules.isEmpty()
			? null
			: SurfaceRules.sequence(rules.toArray(SurfaceRules.RuleSource[]::new));
	}

	private static SurfaceRules.RuleSource findUniqueSurfaceRule(
		Map<String, Map<String, SurfaceRules.RuleSource>> surfaceRulesByDimension,
		ResourceKey<Biome> biome
	) {
		String biomeId = VersionedId.GetId(biome).toString();
		SurfaceRules.RuleSource result = null;

		for (Map<String, SurfaceRules.RuleSource> dimensionRules
			: surfaceRulesByDimension.values()) {
			SurfaceRules.RuleSource candidate = dimensionRules.get(biomeId);

			if (candidate == null) {
				continue;
			}

			if (result != null) {
				return null;
			}

			result = candidate;
		}

		return result;
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