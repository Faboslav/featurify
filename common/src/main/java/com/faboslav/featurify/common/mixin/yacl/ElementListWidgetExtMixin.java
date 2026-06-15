package com.faboslav.featurify.common.mixin.yacl;

import org.spongepowered.asm.mixin.Mixin;

//? if <= 1.20.1 {
/*import dev.isxander.yacl3.gui.ElementListWidgetExt;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ElementListWidgetExt.class, remap = false)
public interface ElementListWidgetExtMixin
{
	@Invoker("resetSmoothScrolling")
	void featurify$resetSmoothScrolling();
}
*///?} else {
import dev.isxander.yacl3.gui.OptionListWidget;

@Mixin(value = OptionListWidget.class, remap = false)
public interface ElementListWidgetExtMixin
{
}
//?}