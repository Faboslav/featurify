package com.faboslav.featurify.common.mixin.biome;

import com.faboslav.featurify.common.Featurify;
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
	@Nullable
	private volatile SurfaceRules.RuleSource featurify$cachedSurfaceRules;


	@WrapMethod(method = "surfaceRule")
	private SurfaceRules.RuleSource featurify$addReplacementSurfaceRules(
		Operation<SurfaceRules.RuleSource> original
	) {
		SurfaceRules.RuleSource cachedSurfaceRules = this.featurify$cachedSurfaceRules;

		if (cachedSurfaceRules != null) {
			return cachedSurfaceRules;
		}

		synchronized (this) {
			Featurify.getLogger().info("surfaceRule");
			cachedSurfaceRules = this.featurify$cachedSurfaceRules;

			if (cachedSurfaceRules == null) {
				SurfaceRules.RuleSource originalRules = original.call();
				SurfaceRules.RuleSource replacementRules =
					FeaturifySurfaceRuleSources.createReplacementRules();

				cachedSurfaceRules = replacementRules == null
					? originalRules
					: SurfaceRules.sequence(
					replacementRules,
					originalRules
				);

				this.featurify$cachedSurfaceRules = cachedSurfaceRules;
			}
		}

		return cachedSurfaceRules;
	}

	@Override
	public void featurify$clearSurfaceRules() {
		this.featurify$cachedSurfaceRules = null;
	}
}