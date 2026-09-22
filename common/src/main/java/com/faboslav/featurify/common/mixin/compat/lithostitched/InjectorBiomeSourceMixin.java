//? if lithostitched {
package com.faboslav.featurify.common.mixin.compat.lithostitched;

import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import com.faboslav.featurify.common.worldgen.biome.compat.FeaturifyBiomeFilter;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.worldgen.lithostitched.impl.worldgen.biomeinjector.internal.InjectorBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? if >= 26.3 {
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.levelgen.densityfunction.DensitySamplerSet;
//?}

@Pseudo
@Mixin(value = InjectorBiomeSource.class, remap = false)
public abstract class InjectorBiomeSourceMixin
{
	@Shadow
	@Final
	private BiomeSource directDelegate;

	@ModifyReturnValue(
		method = "getNoiseBiome",
		at = @At("RETURN"),
		require = 0
	)
	private Holder<Biome> featurify$filterInjectedBiome(
		Holder<Biome> original,
		int quartX,
		int quartY,
		int quartZ,
		//? if >= 26.3 {
		BiomeResolver baseResolver,
		Climate.Sampler sampler,
		DensitySamplerSet samplers
		//?} else {
		//Climate.Sampler sampler
		//?}
	) {
		return FeaturifyBiomeFilter.getReplacementBiome(
			original,
			quartX,
			quartY,
			quartZ,
			sampler,
			this.directDelegate
		);
	}
}
//?}