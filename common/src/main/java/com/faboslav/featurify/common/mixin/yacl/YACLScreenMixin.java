package com.faboslav.featurify.common.mixin.yacl;

import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.api.FeaturifyYACLScreen;
import com.faboslav.featurify.common.versions.VersionedGui;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = YACLScreen.class)
public abstract class YACLScreenMixin extends Screen implements FeaturifyYACLScreen
{
	@Unique
	private boolean featurify$isFeaturifyScreen = false;

	protected YACLScreenMixin(Component component) {
		super(component);
	}

	@Override
	public boolean featurify$isFeaturifyScreen() {
		return this.featurify$isFeaturifyScreen;
	}

	@Override
	public void featurify$markAsFeaturifyScreen() {
		this.featurify$isFeaturifyScreen = true;
	}

	@Inject(
		method = "onClose",
		at = @At("HEAD")
	)
	public void featurify$onCloseHead(CallbackInfo ci) {
		if (this.minecraft != null) {
			if (VersionedGui.getScreen(this.minecraft) instanceof YACLScreen yaclsScreen) {
				FeaturifyClient.getConfigScreen().saveScreenState(yaclsScreen);
			}
		}
	}

	@Inject(
		method = "onClose",
		at = @At("TAIL")
	)
	public void featurify$onCloseTail(CallbackInfo ci) {
		if (this.minecraft != null) {
			if (VersionedGui.getScreen(this.minecraft) instanceof YACLScreen yaclsScreen) {
				FeaturifyClient.getConfigScreen().loadScreenState(yaclsScreen);
			}
		}
	}
}
