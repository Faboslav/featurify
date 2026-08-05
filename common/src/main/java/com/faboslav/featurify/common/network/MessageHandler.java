package com.faboslav.featurify.common.network;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.network.packet.ConfigStatusToClientPacket;
import com.faboslav.featurify.common.network.packet.ConfigSyncRequestToClientPacket;
import com.faboslav.featurify.common.network.packet.ConfigSyncToClientPacket;
import com.faboslav.featurify.common.network.packet.ConfigSyncToServerPacket;

public final class MessageHandler
{
	public static final NetworkChannel DEFAULT_CHANNEL = new NetworkChannel(Featurify.makeId("network"), 1);

	public static void init() {
		DEFAULT_CHANNEL.register(ConfigStatusToClientPacket.TYPE);
		DEFAULT_CHANNEL.register(ConfigSyncRequestToClientPacket.TYPE);
		DEFAULT_CHANNEL.register(ConfigSyncToClientPacket.TYPE);
		DEFAULT_CHANNEL.register(ConfigSyncToServerPacket.TYPE);
	}
}
