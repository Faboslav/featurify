package com.faboslav.featurify.common.mixin.biome;

import com.faboslav.featurify.common.api.FeaturifyMultiNoiseBiomeSource;
import com.faboslav.featurify.common.worldgen.biome.BiomeParameterReplacer;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin implements FeaturifyMultiNoiseBiomeSource
{
	@Unique
	private final Map<Climate.ParameterList<Holder<Biome>>, Climate.ParameterList<Holder<Biome>>> featurify$parameters = new IdentityHashMap<>();

	@Unique
	public void featurify$clearParameters() {
		synchronized (this.featurify$parameters) {
			this.featurify$parameters.replaceAll(BiomeParameterReplacer::createReplacementList);
		}
	}

	@ModifyReturnValue(
		method = "parameters",
		at = @At("RETURN")
	)
	private Climate.ParameterList<Holder<Biome>> featurify$replaceBiomeParameters(
		Climate.ParameterList<Holder<Biome>> originalParameters
	) {
		synchronized (this.featurify$parameters) {
			return this.featurify$parameters.computeIfAbsent(
				originalParameters,
				key -> BiomeParameterReplacer.createReplacementList(key, null)
			);
		}
	}
}