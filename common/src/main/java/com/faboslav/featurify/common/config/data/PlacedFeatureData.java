package com.faboslav.featurify.common.config.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents @Pla
 */
public final class PlacedFeatureData
{
	public static boolean IS_DISABLED_DEFAULT_VALUE = false;

	private boolean isDisabled = IS_DISABLED_DEFAULT_VALUE;
	private final List<String> defaultBiomes;
	private List<String> biomes;

	public PlacedFeatureData(
		List<String> biomes
	) {
		this.defaultBiomes = new ArrayList<>(biomes);
		this.biomes = new ArrayList<>(biomes);
	}

	public boolean isUsingDefaultIsDisabled() {
		var biomes = new ArrayList<>(this.biomes);
		var defaultBiomes = new ArrayList<>(this.defaultBiomes);

		Collections.sort(biomes);
		Collections.sort(defaultBiomes);

		return this.isDisabled == IS_DISABLED_DEFAULT_VALUE && biomes.equals(defaultBiomes);
	}

	public boolean isUsingDefaultValues() {
		return this.isUsingDefaultIsDisabled();
	}

	/**
	 * Used in {@link com.faboslav.featurify.common.mixin.feature.PlacedFeatureMixin} to prevent specific placed features generation
	 */
	public boolean isDisabled() {
		return this.isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public List<String> getDefaultBiomes() {
		return this.defaultBiomes;
	}

	public List<String> getBiomes() {
		return this.biomes;
	}

	public List<String> getAdditionalBiomes() {
		var additionalBiomes = new ArrayList<>(this.getBiomes());
		additionalBiomes.removeAll(this.getDefaultBiomes());

		return additionalBiomes;
	}

	public List<String> getRemovedBiomes() {
		var removedBiomes = new ArrayList<>(this.getDefaultBiomes());
		removedBiomes.removeAll(this.getBiomes());

		return removedBiomes;
	}

	public void setBiomes(List<String> biomes) {
		this.biomes = biomes;
	}
}
