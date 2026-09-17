package com.faboslav.featurify.fabric.mixin;

import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public abstract class MainMixin
{
	@Inject(
		method = "main",
		at = @At(
			value = "INVOKE",
			target = /*? if >= 1.21.11 {*/"Lnet/minecraft/util/Util;startTimerHackThread()V"/*?} else {*//*"Lnet/minecraft/Util;startTimerHackThread()V"*//*?}*/,
			shift = At.Shift.AFTER
		)
	)
	private static void featurify$initFeaturify(CallbackInfo ci) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
			return;
		}

		LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
		UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
	}
}
