package com.faboslav.featurify.common.network;

import com.faboslav.featurify.common.network.base.PacketType;

public interface Packet<T extends Packet<T>>
{
	PacketType<T> type();
}
