package com.faboslav.featurify.common.network.platform;

import com.faboslav.featurify.common.network.base.Network;
import net.minecraft.resources.Identifier;

public interface NetworkPlatform
{
	Network create(Identifier channel, int protocolVersion);
}
