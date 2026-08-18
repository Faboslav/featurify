package com.faboslav.featurify.common.mixin.compat.lithostitched;

import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

//? if lithostitched {
import com.faboslav.featurify.common.worldgen.biome.compat.LithostitchedBiomeFilter;
import dev.worldgen.lithostitched.impl.worldgen.biomeinjector.internal.InjectorBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Shadow;

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
		Climate.Sampler sampler
	) {
		return LithostitchedBiomeFilter.filter(
			original,
			quartX,
			quartY,
			quartZ,
			sampler,
			this.directDelegate
		);
	}
}
//?} else {
/*import net.minecraft.world.level.biome.BiomeSource;

@Mixin(value = BiomeSource.class)
public abstract class InjectorBiomeSourceMixin
{
}*///?}
