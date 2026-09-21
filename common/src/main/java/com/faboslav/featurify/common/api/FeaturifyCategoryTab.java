package com.faboslav.featurify.common.api;

import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.Nullable;

public interface FeaturifyCategoryTab
{
	@Nullable
	Button featurify$getToggleGroupsButton();

	void featurify$updateToggleGroupsButton();
}
