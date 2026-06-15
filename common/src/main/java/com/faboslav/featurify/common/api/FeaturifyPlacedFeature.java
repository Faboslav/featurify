package com.faboslav.featurify.common.api;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public interface FeaturifyPlacedFeature
{
	void featurify$setIdentifier(@Nullable Identifier placedFeatureIdentifier);

	@Nullable
	Identifier featurify$getIdentifier();

}
