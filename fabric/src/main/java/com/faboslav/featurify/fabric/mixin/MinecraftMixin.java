package com.faboslav.featurify.fabric.mixin;

import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin
{
	@Inject(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;",
			shift = At.Shift.AFTER
		)
	)
	private void featurify$initFeaturify(CallbackInfo ci) {
		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
