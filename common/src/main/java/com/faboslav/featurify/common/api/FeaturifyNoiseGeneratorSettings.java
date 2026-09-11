package com.faboslav.featurify.common.api;

import net.minecraft.world.level.levelgen.SurfaceRules;

public interface FeaturifyNoiseGeneratorSettings
{
	void featurify$clearSurfaceRules();

	SurfaceRules.RuleSource featurify$getSurfaceRule();
}
