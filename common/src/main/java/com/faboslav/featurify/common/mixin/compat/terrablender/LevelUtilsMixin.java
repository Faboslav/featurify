package com.faboslav.featurify.common.mixin.compat.terrablender;

import org.spongepowered.asm.mixin.Mixin;

//? if terrablender {
import com.faboslav.featurify.common.worldgen.biome.compat.TerraBlenderBiomeFilter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import terrablender.api.Region;
import terrablender.util.LevelUtils;

import java.util.function.Consumer;

@Pseudo
@Mixin(value = LevelUtils.class, remap = false)
public abstract class LevelUtilsMixin
{
	@WrapOperation(
		//? if >=26.2 {
		method = "lambda$initializeBiomes$0",
		//?} else {
		/*method = "lambda$initializeBiomes$1",
		*///?}
		at = @At(
			value = "INVOKE",
			target = "Lterrablender/api/Region;addBiomes(Lnet/minecraft/core/Registry;Ljava/util/function/Consumer;)V"
		),
		require = 0
	)
	private static void featurify$filterPossibleBiomes(
		Region region,
		Registry<Biome> biomeRegistry,
		Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper,
		Operation<Void> original
	) {
		original.call(
			region,
			biomeRegistry,
			(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) pair -> {
				ResourceKey<Biome> replacementBiome =
					TerraBlenderBiomeFilter.getReplacementBiomeKey(pair.getSecond());

				if (replacementBiome == null) {
					return;
				}

				mapper.accept(Pair.of(
					pair.getFirst(),
					replacementBiome
				));
			}
		);
	}
}
//?} else {
/*@Mixin(value = Climate.ParameterList.class)
/*public abstract class LevelUtilsMixin
{

}*///?}