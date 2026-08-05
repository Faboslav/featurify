package com.faboslav.featurify.common.network.platform;

import com.faboslav.featurify.common.network.base.Network;
import net.minecraft.resources.ResourceLocation;

public interface NetworkPlatform
{
	Network create(ResourceLocation channel, int protocolVersion);
}
