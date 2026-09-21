//? if >= 26.3 {
package com.faboslav.featurify.common.mixin.feature;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.api.FeaturifyPlacedFeature;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.FeaturePlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FeaturePlacer.class)
public abstract class FeaturePlacerMixin
{
	@WrapMethod(
		method = "place(Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Z)Z"
	)
	private boolean featurify$place(
		PlacedFeature placedFeature,
		RandomSource random,
		BlockPos origin,
		boolean biomeCheck,
		Operation<Boolean> original
	) {
		var config = Featurify.getConfig();

		if (config.disableAllPlacedFeatures) {
			return false;
		}

		var placedFeatureId = ((FeaturifyPlacedFeature) (Object) placedFeature).featurify$getIdentifier();
		var placedFeatureData = config.getPlacedFeatureData();

		if (placedFeatureId == null || !placedFeatureData.containsKey(placedFeatureId.toString())) {
			return original.call(placedFeature, random, origin, biomeCheck);
		}

		if (placedFeatureData.get(placedFeatureId.toString()).isDisabled()) {
			return false;
		}

		return original.call(placedFeature, random, origin, biomeCheck);
	}
}
//?}
