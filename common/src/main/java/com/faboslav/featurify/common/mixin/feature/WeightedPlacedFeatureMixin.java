package com.faboslav.featurify.common.mixin.feature;

import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedPlacedFeature.class)
public interface WeightedPlacedFeatureMixin
{
	@Mutable
	@Accessor
	void setChance(float chance);
}
