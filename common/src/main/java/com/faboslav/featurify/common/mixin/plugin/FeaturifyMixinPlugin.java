package com.faboslav.featurify.common.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FeaturifyMixinPlugin implements IMixinConfigPlugin
{
	private String mixinPackage;

	@Override
	public void onLoad(String mixinPackage) {
		this.mixinPackage = mixinPackage;
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.equals("com.faboslav.featurify.common.mixin.WorldOpenFlowsMixin")) {
			return this.isClassAvailable("me.earth.mc_runtime_test.McRuntimeTest");
		}

		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		List<String> mixins = new ArrayList<>();

		if (this.mixinPackage.equals("com.faboslav.featurify.common.mixin")) {
			// TerraBlender
			if (this.isClassAvailable("terrablender.util.LevelUtils")) {
				mixins.add("compat.terrablender.LevelUtilsMixin");
			}

			// TerraBlender
			if (this.isClassAvailable("terrablender.core.TerraBlender")) {
				mixins.add("compat.terrablender.ParameterListMixin");
			}

			// Lithostitched
			if (this.isClassAvailable("dev.worldgen.lithostitched.impl.worldgen.biomeinjector.internal.InjectorBiomeSource")) {
				mixins.add("compat.lithostitched.InjectorBiomeSourceMixin");
			}
		}

		// Blueprint
		if (this.mixinPackage.equals("com.faboslav.featurify.neoforge.mixin")
			&& this.isClassAvailable("com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSource")
			&& this.isClassAvailable("com.faboslav.featurify.neoforge.mixin.biome.blueprint.ModdedBiomeSourceMixin"))
		{
			mixins.add("biome.blueprint.ModdedBiomeSourceMixin");
		}

		// Blueprint
		if (this.mixinPackage.equals("com.faboslav.featurify.forge.mixin")
			&& this.isClassAvailable("com.teamabnormals.blueprint.common.world.modification.ModdedBiomeSource")
			&& this.isClassAvailable("com.faboslav.featurify.forge.mixin.biome.blueprint.ModdedBiomeSourceMixin"))
		{
			mixins.add("biome.blueprint.ModdedBiomeSourceMixin");
		}

		return mixins;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	private boolean isClassAvailable(String className) {
		String classPath = className.replace('.', '/') + ".class";
		return getClass().getClassLoader().getResource(classPath) != null;
	}
}