package com.faboslav.featurify.common.mixin.biome;

import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Climate.ParameterPoint.class)
public interface ClimateParameterPointAccessor
{
	@Invoker("fitness")
	long featurify$fitness(Climate.TargetPoint targetPoint);
}