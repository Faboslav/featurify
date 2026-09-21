package com.faboslav.featurify.common.mixin.feature;

import com.faboslav.featurify.common.api.FeaturifyPlacedFeature;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if < 26.3 {
/*import com.faboslav.featurify.common.Featurify;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
*///?}

@Mixin(PlacedFeature.class)
public abstract class PlacedFeatureMixin implements FeaturifyPlacedFeature
{
	@Unique
	@Nullable
	public Identifier featurify$resourceLocation = null;

	@Override
	public void featurify$setIdentifier(@Nullable Identifier resourceLocation) {
		this.featurify$resourceLocation = resourceLocation;
	}

	@Override
	public @Nullable Identifier featurify$getIdentifier() {
		return this.featurify$resourceLocation;
	}

	//? if < 26.3 {
	/*@WrapMethod(
		method = "placeWithContext"
	)
	private boolean featurify$placeWithContext(
		PlacementContext context,
		RandomSource source,
		BlockPos pos,
		Operation<Boolean> original
	) {
			var config = Featurify.getConfig();

			if(config.disableAllPlacedFeatures) {
				return false;
			}

			var placedFeatureId = this.featurify$getIdentifier();
			var placedFeatureData = config.getPlacedFeatureData();

			if(placedFeatureId == null || !placedFeatureData.containsKey(placedFeatureId.toString())) {
				return original.call(context, source, pos);
			}

			if(placedFeatureData.get(placedFeatureId.toString()).isDisabled()) {
				return false;
			}

			return original.call(context, source, pos);
	}
	*///?}
}
