package com.faboslav.featurify.common.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.level.biome.Climate$RTree$Leaf")
public interface ClimateRTreeLeafAccessor
{
	@Accessor("value")
	Object featurify$getValue();
}
