package com.faboslav.featurify.common.mixin;

import com.faboslav.featurify.common.api.FeaturifyCategoryTab;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(TabManager.class)
public abstract class TabManagerMixin
{
	@Shadow
	@Final
	private Consumer<AbstractWidget> addWidget;

	@Shadow
	@Final
	private Consumer<AbstractWidget> removeWidget;

	@Shadow
	@Nullable
	private Tab currentTab;

	@WrapMethod(
		//? if >= 26.2 {
		method = "setCurrentTab(Lnet/minecraft/client/gui/components/tabs/Tab;ZZ)V"
		//?} else {
		//method = "setCurrentTab(Lnet/minecraft/client/gui/components/tabs/Tab;Z)V"
		//?}
	)
	private void featurify$setCurrentTab(
		Tab tab,
		boolean playClickSound,
		//? if >= 26.2 {
		boolean flag,
		//?}
		Operation<Void> original
	) {
		var previousTab = this.currentTab;

		//? if >= 26.2 {
		original.call(tab, playClickSound, flag);
		//?} else {
		//original.call(tab, playClickSound);
		//?}

		if (this.currentTab == previousTab) {
			return;
		}

		if (previousTab instanceof FeaturifyCategoryTab previousCategoryTab && previousCategoryTab.featurify$getToggleGroupsButton() != null) {
			this.removeWidget.accept(previousCategoryTab.featurify$getToggleGroupsButton());
		}

		if (this.currentTab instanceof FeaturifyCategoryTab currentCategoryTab && currentCategoryTab.featurify$getToggleGroupsButton() != null) {
			this.addWidget.accept(currentCategoryTab.featurify$getToggleGroupsButton());
		}
	}
}
