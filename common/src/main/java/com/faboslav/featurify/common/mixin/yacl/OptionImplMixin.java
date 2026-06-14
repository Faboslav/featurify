package com.faboslav.featurify.common.mixin.yacl;

import com.faboslav.featurify.common.api.FeaturifyOption;
import dev.isxander.yacl3.impl.OptionImpl;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;

@Mixin(value = OptionImpl.class, remap = false)
public abstract class OptionImplMixin implements FeaturifyOption
{
	@Mutable
	@Final
	@Shadow
	private Component name;

	@Unique
	public void featurify$setName(Component name) {
		this.name = name;
	}
}
