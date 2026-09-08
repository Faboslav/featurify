//? if biolith {
package com.faboslav.featurify.common.mixin.compat.biolith;

import com.faboslav.featurify.common.mixin.biome.ClimateRTreeLeafAccessor;
import com.faboslav.featurify.common.worldgen.biome.compat.FeaturifyBiomeFilter;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.biolith.api.biome.BiolithFittestNodes;
import com.terraformersmc.biolith.impl.biome.DimensionBiomePlacement;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(value = DimensionBiomePlacement.class, remap = false)
public abstract class DimensionBiomePlacementMixin
{
	@ModifyReturnValue(
		method = "getReplacement",
		at = @At("RETURN"),
		require = 0
	)
	private Holder<Biome> featurify$filterBiolithReplacement(
		Holder<Biome> original,
		int x,
		int y,
		int z,
		Climate.TargetPoint noisePoint,
		BiolithFittestNodes<Holder<Biome>> fittestNodes
	) {
		Object originalLeaf = fittestNodes.ultimate();

		return FeaturifyBiomeFilter.getReplacementBiome(
			original,
			() -> (Holder<Biome>) ((ClimateRTreeLeafAccessor) originalLeaf).featurify$getValue()
		);
	}
}
//?}
