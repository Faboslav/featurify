package com.faboslav.featurify.common.config.data;

import java.util.*;

public final class PlacedFeatureData
{
	public static boolean IS_DISABLED_DEFAULT_VALUE = false;
	public static float MIN_CHANCE = 0.0F;
	public static float MAX_CHANCE = 1.0F;

	private boolean isDisabled = IS_DISABLED_DEFAULT_VALUE;
	private final List<String> defaultBiomes;
	private List<String> biomes;
	private Map<String, Float> defaultWeightedPlacedFeatures;
	private Map<String, Float> weightedPlacedFeatures;

	public PlacedFeatureData(
		List<String> biomes,
		Map<String, Float> weightedPlacedFeatures
	) {
		this.defaultBiomes = new ArrayList<>(biomes);
		this.biomes = new ArrayList<>(biomes);
		this.defaultWeightedPlacedFeatures = new TreeMap<>(weightedPlacedFeatures);
		this.weightedPlacedFeatures = new TreeMap<>(weightedPlacedFeatures);
	}

	public boolean isUsingDefaultIsDisabled() {
		return this.isDisabled == IS_DISABLED_DEFAULT_VALUE && biomes.equals(defaultBiomes);
	}

	public boolean isUsingDefaultValues() {
		return this.isUsingDefaultIsDisabled() && this.isUsingDefaultBiomes() && this.isUsingDefaultWeightedPlacedFeatures();
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

	public boolean isUsingDefaultBiomes() {
		var biomes = new ArrayList<>(this.biomes);
		var defaultBiomes = new ArrayList<>(this.defaultBiomes);

		Collections.sort(biomes);
		Collections.sort(defaultBiomes);

		return biomes.equals(defaultBiomes);
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

	public boolean isUsingDefaultWeightedPlacedFeatures() {
		return this.weightedPlacedFeatures.equals(this.defaultWeightedPlacedFeatures);
	}

	public Map<String, Float> getDefaultWeightedPlacedFeatures() {
		return this.defaultWeightedPlacedFeatures;
	}

	public Map<String, Float> getWeightedPlacedFeatures() {
		return this.weightedPlacedFeatures;
	}
}
