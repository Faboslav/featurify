package com.faboslav.featurify.fabric.platform;

import com.faboslav.featurify.common.network.base.Network;
import com.faboslav.featurify.common.network.platform.NetworkPlatform;
import com.faboslav.featurify.fabric.network.FabricNetwork;
import net.minecraft.resources.ResourceLocation;

public final class FabricPlatformNetwork implements NetworkPlatform
{
	@Override
	public Network create(ResourceLocation channel, int protocolVersion) {
		return new FabricNetwork(channel, protocolVersion);
	}
}
