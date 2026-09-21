package com.faboslav.featurify.common.mixin.yacl;

import com.faboslav.featurify.common.api.FeaturifyCategoryTab;
import com.faboslav.featurify.common.api.FeaturifyYACLScreen;
import com.faboslav.featurify.common.util.YACLUtil;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.SearchFieldWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = YACLScreen.CategoryTab.class, remap = false)
public abstract class CategoryTabMixin implements FeaturifyCategoryTab
{
	@Shadow
	@Final
	private SearchFieldWidget searchField;

	@Shadow
	@Final
	public Button undoButton;

	@Shadow
	@Final
	public Button saveFinishedButton;

	@Unique
	@Nullable
	private OptionListWidget featurify$optionListWidget = null;

	@Unique
	@Nullable
	private Button featurify$toggleGroupsButton = null;

	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void featurify$init(YACLScreen screen, ConfigCategory category, ScreenRectangle tabArea, CallbackInfo ci) {
		if (!((FeaturifyYACLScreen) screen).featurify$isFeaturifyScreen()) {
			return;
		}

		this.featurify$optionListWidget = YACLUtil.getOptionListWidget(this);
		this.searchField.setY(this.searchField.getY() - 22);

		this.featurify$toggleGroupsButton = Button.builder(Component.empty(), button -> this.featurify$toggleGroups())
			.pos(this.saveFinishedButton.getX(), this.undoButton.getY() - 22)
			.size(this.saveFinishedButton.getWidth(), this.saveFinishedButton.getHeight())
			.build();

		this.featurify$updateToggleGroupsButton();
	}

	@Override
	@Nullable
	public Button featurify$getToggleGroupsButton() {
		return this.featurify$toggleGroupsButton;
	}

	@Unique
	private void featurify$toggleGroups() {
		if (this.featurify$optionListWidget == null) {
			return;
		}

		var expand = !this.featurify$hasExpandedGroup();

		for (OptionListWidget.Entry entry : this.featurify$optionListWidget.children()) {
			if (entry instanceof OptionListWidget.GroupSeparatorEntry groupSeparatorEntry) {
				groupSeparatorEntry.setExpanded(expand);
			}
		}

		this.featurify$optionListWidget.setScrollAmount(0);
		this.featurify$updateToggleGroupsButton();
	}

	@Override
	public void featurify$updateToggleGroupsButton() {
		if (this.featurify$toggleGroupsButton == null || this.featurify$optionListWidget == null) {
			return;
		}

		var labelKey = this.featurify$hasExpandedGroup() ? "gui.featurify.label.collapse_all" : "gui.featurify.label.expand_all";
		this.featurify$toggleGroupsButton.setMessage(Component.translatable(labelKey));
		this.featurify$toggleGroupsButton.setTooltip(Tooltip.create(Component.translatable(labelKey + ".tooltip")));
	}

	@Unique
	private boolean featurify$hasExpandedGroup() {
		if (this.featurify$optionListWidget == null) {
			return false;
		}

		for (OptionListWidget.Entry entry : this.featurify$optionListWidget.children()) {
			if (entry instanceof OptionListWidget.GroupSeparatorEntry groupSeparatorEntry && groupSeparatorEntry.isExpanded()) {
				return true;
			}
		}

		return false;
	}
}
