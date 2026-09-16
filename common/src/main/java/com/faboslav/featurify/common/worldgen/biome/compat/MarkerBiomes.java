package com.faboslav.featurify.common.worldgen.biome.compat;

import java.util.Set;

public final class MarkerBiomes
{
	private static final Set<String> MARKER_BIOME_IDS = Set.of(
		"terrablender:deferred_placeholder",
		"blueprint:original_source_marker"
	);

	public static boolean isMarkerBiome(String biomeId) {
		return MARKER_BIOME_IDS.contains(biomeId);
	}
}
