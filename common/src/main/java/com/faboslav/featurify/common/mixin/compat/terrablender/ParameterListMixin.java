//? if terrablender {
package com.faboslav.featurify.common.mixin.compat.terrablender;

import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import com.faboslav.featurify.common.worldgen.biome.compat.FeaturifyBiomeFilter;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Pseudo
@Mixin(value = Climate.ParameterList.class, priority = 2000, remap = false)
public abstract class ParameterListMixin
{
	@ModifyArg(
		method = "initializeForTerraBlender",
		at = @At(
			value = "INVOKE",
			target = "Lterrablender/api/Region;addBiomes(Lnet/minecraft/core/Registry;Ljava/util/function/Consumer;)V"
		),
		require = 0
	)
	private Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> featurify$filterRegionBiomes(
		Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper
	) {
		return pair -> {
			ResourceKey<Biome> replacementBiome = FeaturifyBiomeFilter.getReplacementBiomeKey(pair.getSecond());

			if (replacementBiome == null) {
				return;
			}

			mapper.accept(Pair.of(pair.getFirst(), replacementBiome));
		};
	}
}
//?}