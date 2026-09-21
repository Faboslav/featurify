package com.faboslav.featurify.common.mixin.yacl;

import com.faboslav.featurify.common.api.FeaturifyCategoryTab;
import com.faboslav.featurify.common.api.FeaturifyYACLScreen;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OptionListWidget.GroupSeparatorEntry.class, remap = false)
public abstract class GroupSeparatorEntryMixin
{
	@Shadow
	@Final
	protected Screen screen;

	@Inject(
		method = "setExpanded",
		at = @At("TAIL")
	)
	private void featurify$setExpanded(boolean expanded, CallbackInfo ci) {
		if (!(this.screen instanceof YACLScreen yaclScreen) || !((FeaturifyYACLScreen) yaclScreen).featurify$isFeaturifyScreen() || yaclScreen.tabNavigationBar == null) {
			return;
		}

		if (yaclScreen.tabNavigationBar.getTabManager().getCurrentTab() instanceof FeaturifyCategoryTab categoryTab) {
			categoryTab.featurify$updateToggleGroupsButton();
		}
	}
}
