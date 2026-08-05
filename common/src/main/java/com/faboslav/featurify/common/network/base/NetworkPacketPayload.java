//? if >= 1.20.2 {
/*package com.faboslav.featurify.common.network.base;

import com.faboslav.featurify.common.network.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NetworkPacketPayload<T extends Packet<T>>(
	T packet,
	Type<NetworkPacketPayload<T>> type
) implements CustomPacketPayload
{
	public NetworkPacketPayload(T packet, ResourceLocation channel) {
		this(packet, packet.type().type(channel));
	}
}
*///?}
