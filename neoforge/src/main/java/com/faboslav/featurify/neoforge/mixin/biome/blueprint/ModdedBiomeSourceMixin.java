//? if blueprint {
/*package com.faboslav.featurify.neoforge.mixin.biome.blueprint;

import com.faboslav.featurify.common.worldgen.biome.compat.FeaturifyBiomeFilter;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = ModdedBiomeSource.class, remap = false)
public abstract class ModdedBiomeSourceMixin
{
	@Shadow
	@Final
	private BiomeSource originalSource;

	@ModifyReturnValue(
		method = "getNoiseBiome",
		at = @At("RETURN"),
		require = 0
	)
	private Holder<Biome> featurify$filterModdedBiome(
		Holder<Biome> original,
		int quartX,
		int quartY,
		int quartZ,
		Climate.Sampler sampler
	) {
		return FeaturifyBiomeFilter.getReplacementBiome(
			original,
			quartX,
			quartY,
			quartZ,
			sampler,
			this.originalSource
		);
	}
}
*///?}
