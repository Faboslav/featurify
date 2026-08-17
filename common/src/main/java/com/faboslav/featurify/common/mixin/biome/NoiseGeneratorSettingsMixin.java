package com.faboslav.featurify.common.mixin.biome;

import com.faboslav.featurify.common.api.FeaturifyNoiseGeneratorSettings;
import com.faboslav.featurify.common.worldgen.biome.FeaturifySurfaceRuleSources;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NoiseGeneratorSettings.class)
public abstract class NoiseGeneratorSettingsMixin implements FeaturifyNoiseGeneratorSettings
{
	@Unique
	private volatile boolean featurify$replacementRulesComputed;

	@Unique
	@Nullable
	private volatile SurfaceRules.RuleSource featurify$cachedReplacementRules;

	@WrapMethod(method = "surfaceRule")
	private SurfaceRules.RuleSource featurify$addReplacementSurfaceRules(
		Operation<SurfaceRules.RuleSource> original
	) {
		if (!this.featurify$replacementRulesComputed) {
			synchronized (this) {
				if (!this.featurify$replacementRulesComputed) {
					this.featurify$cachedReplacementRules = FeaturifySurfaceRuleSources.createReplacementRules();
					this.featurify$replacementRulesComputed = true;
				}
			}
		}

		SurfaceRules.RuleSource originalRules = original.call();
		SurfaceRules.RuleSource replacementRules = this.featurify$cachedReplacementRules;

		if(replacementRules == null) {
			return originalRules;
		}

		return SurfaceRules.sequence(replacementRules, originalRules);
	}

	@Override
	public void featurify$clearSurfaceRules() {
		synchronized (this) {
			this.featurify$replacementRulesComputed = false;
			this.featurify$cachedReplacementRules = null;
		}
	}
}