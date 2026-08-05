package com.faboslav.featurify.common.config.data;

public final class BiomeData
{
	public static final boolean IS_DISABLED_DEFAULT_VALUE = false;
	public static final String REPLACEMENT_BIOME_DEFAULT_VALUE = "";

	private boolean isDisabled = IS_DISABLED_DEFAULT_VALUE;
	private String replacementBiome = REPLACEMENT_BIOME_DEFAULT_VALUE;

	public BiomeData() {
	}

	public boolean isUsingDefaultIsDisabled() {
		return this.isDisabled == IS_DISABLED_DEFAULT_VALUE;
	}

	public boolean isUsingDefaultReplacementBiome() {
		return this.replacementBiome.equals(REPLACEMENT_BIOME_DEFAULT_VALUE);
	}

	public boolean isUsingDefaultValues() {
		return this.isUsingDefaultIsDisabled()
			   && this.isUsingDefaultReplacementBiome();
	}

	/**
	 * Used in {@link com.faboslav.featurify.common.mixin.biome} to prevent specific biome generation.
	 */
	public boolean isDisabled() {
		return this.isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getReplacementBiome() {
		return this.replacementBiome;
	}

	public void setReplacementBiome(String biome) {
		this.replacementBiome = biome;
	}
}