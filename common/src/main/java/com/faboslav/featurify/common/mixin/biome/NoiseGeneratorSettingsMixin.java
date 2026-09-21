package com.faboslav.featurify.common.mixin.biome;

import com.faboslav.featurify.common.api.FeaturifyNoiseGeneratorSettings;
import com.faboslav.featurify.common.worldgen.biome.FeaturifySurfaceRuleSources;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

//? if >= 26.3 {
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
//?} else {
//import net.minecraft.world.level.levelgen.SurfaceRules;
//?}

@Mixin(NoiseGeneratorSettings.class)
public abstract class NoiseGeneratorSettingsMixin implements FeaturifyNoiseGeneratorSettings
{
	//? if >= 26.3 {
	@Shadow
	@Final
	private Holder<MaterialRule> materialRule;
	//?} else {
	/*@Shadow
	@Final
	private SurfaceRules.RuleSource surfaceRule;
	*///?}

	@Unique
	private volatile boolean featurify$replacementRulesComputed;

	@Unique
	@Nullable
	//? if >= 26.3 {
	private volatile MaterialRule featurify$cachedReplacementRules;
	//?} else {
	//private volatile SurfaceRules.RuleSource featurify$cachedReplacementRules;
	//?}

	//? if >= 26.3 {
	@WrapMethod(method = "materialRule")
	private Holder<MaterialRule> featurify$addReplacementSurfaceRules(
		Operation<Holder<MaterialRule>> original
	) {
		if (!this.featurify$replacementRulesComputed) {
			synchronized (this) {
				if (!this.featurify$replacementRulesComputed) {
					this.featurify$cachedReplacementRules = FeaturifySurfaceRuleSources.createReplacementRules();
					this.featurify$replacementRulesComputed = true;
				}
			}
		}

		Holder<MaterialRule> originalRule = original.call();
		MaterialRule replacementRules = this.featurify$cachedReplacementRules;

		if (replacementRules == null) {
			return originalRule;
		}

		return Holder.direct(MaterialRules.sequence(replacementRules, originalRule.value()));
	}
	//?} else {
	/*@WrapMethod(method = "surfaceRule")
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
	*///?}

	@Override
	public void featurify$clearSurfaceRules() {
		synchronized (this) {
			this.featurify$replacementRulesComputed = false;
			this.featurify$cachedReplacementRules = null;
		}
	}

	@Override
	//? if >= 26.3 {
	public MaterialRule featurify$getSurfaceRule() {
		return this.materialRule.value();
	}
	//?} else {
	/*public SurfaceRules.RuleSource featurify$getSurfaceRule() {
		return this.surfaceRule;
	}
	*///?}
}
