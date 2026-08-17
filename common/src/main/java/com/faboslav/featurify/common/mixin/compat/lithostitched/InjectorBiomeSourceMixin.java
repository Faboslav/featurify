package com.faboslav.featurify.common.mixin.compat.lithostitched;

import org.spongepowered.asm.mixin.Mixin;

//? if lithostitched {
import com.faboslav.featurify.common.worldgen.biome.compat.LithostitchedBiomeFilter;
import dev.worldgen.lithostitched.impl.worldgen.biomeinjector.internal.InjectorBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.biome.Climate;

@Mixin(value = InjectorBiomeSource.class, remap = false)
public abstract class InjectorBiomeSourceMixin
{
	@Shadow
	public BiomeResolver baseResolver;

	@ModifyReturnValue(
		method = "getNoiseBiome",
		at = @At("RETURN")
	)
	private Holder<Biome> featurify$filterInjectedBiome(
		Holder<Biome> original,
		int quartX,
		int quartY,
		int quartZ,
		Climate.Sampler sampler
	) {
		return LithostitchedBiomeFilter.filter(
			original,
			quartX,
			quartY,
			quartZ,
			sampler,
			this.baseResolver
		);
	}
}
//?} else {
/*
import net.minecraft.world.level.biome.BiomeSource;

@Mixin(value = BiomeSource.class)
public abstract class InjectorBiomeSourceMixin
{
}*///?}
