package com.faboslav.featurify.common.api;

//? if >= 26.3 {
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
//?} else {
//import net.minecraft.world.level.levelgen.SurfaceRules;
//?}

public interface FeaturifyNoiseGeneratorSettings
{
	void featurify$clearSurfaceRules();

	//? if >= 26.3 {
	MaterialRule featurify$getSurfaceRule();
	//?} else {
	//SurfaceRules.RuleSource featurify$getSurfaceRule();
	//?}
}
