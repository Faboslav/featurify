package com.faboslav.featurify.common.network.base;

import com.faboslav.featurify.common.network.Packet;

public interface ClientboundPacketType<T extends Packet<T>> extends PacketType<T>
{
	Runnable handle(T message);
}
