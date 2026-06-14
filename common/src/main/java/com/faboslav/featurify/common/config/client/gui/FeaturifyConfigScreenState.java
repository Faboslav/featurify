package com.faboslav.featurify.common.config.client.gui;

import java.util.Map;

public record FeaturifyConfigScreenState(
	String lastSearchText,
	double lastScrollAmount,
	Map<String, Boolean> collapsedGroups
)
{
}
