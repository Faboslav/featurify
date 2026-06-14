package com.faboslav.featurify.common.mixin.yacl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

/**
 * Make search also work with descriptions
 */
@Mixin(value = ControllerWidget.class, remap = false)
public abstract class ControllerWidgetMixin
{
	@Unique
	protected String featurify$optionNameString = "";

	@Unique
	protected String featurify$optionDescriptionString = "";

	@Inject(method = "<init>", at = @At("TAIL"))
	public void featurify$init(Controller control, YACLScreen screen, Dimension dim, CallbackInfo ci) {
		this.featurify$optionNameString = control.option().name().getString().toLowerCase();
		this.featurify$optionDescriptionString = control.option().description().text().getString().toLowerCase();
	}

	@WrapMethod(
		method = "matchesSearch"
	)
	public boolean featurify$matchesSearch(String query, Operation<Boolean> original) {
		if (original.call(query)) {
			return true;
		}

		if (Objects.equals(this.featurify$optionNameString, "") || Objects.equals(this.featurify$optionDescriptionString, "")) {
			return false;
		}

		return this.featurify$optionDescriptionString.contains(query.toLowerCase());
	}
}
