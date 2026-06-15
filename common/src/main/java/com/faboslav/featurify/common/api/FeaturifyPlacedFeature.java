package com.faboslav.featurify.common.api;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface FeaturifyPlacedFeature
{
	void featurify$setResourceLocation(@Nullable ResourceLocation placedFeatureResourceLocation);

	@Nullable
	ResourceLocation featurify$getResourceLocation();

}
