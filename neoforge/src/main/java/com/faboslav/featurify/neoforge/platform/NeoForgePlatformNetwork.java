package com.faboslav.featurify.neoforge.platform;

import com.faboslav.featurify.common.network.base.Network;
import com.faboslav.featurify.common.network.platform.NetworkPlatform;
import com.faboslav.featurify.neoforge.network.NeoForgeNetwork;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class NeoForgePlatformNetwork implements NetworkPlatform
{
	private static NeoForgeNetwork network;

	@Override
	public Network create(Identifier channel, int protocolVersion) {
		network = new NeoForgeNetwork(channel, protocolVersion);
		return network;
	}

	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		if (network != null) {
			network.onNetworkSetup(event);
		}
	}
}
