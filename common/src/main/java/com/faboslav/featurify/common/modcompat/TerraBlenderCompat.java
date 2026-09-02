//? if terrablender {
package com.faboslav.featurify.common.modcompat;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.EnumSet;

public final class TerraBlenderCompat implements ModCompat
{
	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.BIOME_PARAMETER_LIST_PROVIDER);
	}

	@Override
	public Climate.ParameterList<Holder<Biome>> getBiomeParameterListReplacement(
		Climate.ParameterList<Holder<Biome>> originalParameters,
		Climate.ParameterList<Holder<Biome>> previousReplacementList
	) {
		if (previousReplacementList instanceof terrablender.worldgen.IExtendedParameterList<?> extendedParameterList && extendedParameterList.isInitialized()) {
			return previousReplacementList;
		}

		return null;
	}
}
//?}
