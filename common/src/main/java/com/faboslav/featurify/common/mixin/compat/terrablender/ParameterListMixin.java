package com.faboslav.featurify.common.mixin.compat.terrablender;

import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;

//? if terrablender {
import com.faboslav.featurify.common.worldgen.biome.TerraBlenderBiomeFilter;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(value = Climate.ParameterList.class, priority = 2000, remap = false)
public abstract class ParameterListMixin
{
	@Dynamic("Targets initializeForTerraBlender added by TerraBlender's mixin")
	@ModifyArg(
		method = "initializeForTerraBlender",
		at = @At(
			value = "INVOKE",
			target = "Lterrablender/api/Region;addBiomes(Lnet/minecraft/core/Registry;Ljava/util/function/Consumer;)V"
		)
	)
	private Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> featurify$filterRegionBiomes(
		Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper
	) {
		return pair -> {
			ResourceKey<Biome> replacementBiome = TerraBlenderBiomeFilter.getReplacementBiomeKey(pair.getSecond());

			if (replacementBiome == null) {
				return;
			}

			mapper.accept(Pair.of(pair.getFirst(), replacementBiome));
		};
	}
}
//?} else {
/*@Mixin(value = Climate.ParameterList.class)
public abstract class ParameterListMixin
{
}*///?}