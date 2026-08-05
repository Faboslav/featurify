package com.faboslav.featurify.common.network.base;

import com.faboslav.featurify.common.network.Packet;
//? if >= 1.20.2 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?} else {
import net.minecraft.network.FriendlyByteBuf;
//?}
import net.minecraft.resources.ResourceLocation;

public interface PacketType<T extends Packet<T>>
{
	ResourceLocation id();

	//? if >= 1.20.2 {
	/*void encode(T message, RegistryFriendlyByteBuf buffer);

	T decode(RegistryFriendlyByteBuf buffer);

	default StreamCodec<RegistryFriendlyByteBuf, NetworkPacketPayload<T>> codec(CustomPacketPayload.Type<NetworkPacketPayload<T>> type) {
		return StreamCodec.of(
			(buf, payload) -> encode(payload.packet(), buf),
			(buf) -> new NetworkPacketPayload<>(decode(buf), type)
		);
	}

	default CustomPacketPayload.Type<NetworkPacketPayload<T>> type(ResourceLocation channel) {
		return new CustomPacketPayload.Type<>(channel.withSuffix("/" + this.id().getPath()));
	}
	*///?} else {
	void encode(T message, FriendlyByteBuf buffer);

	T decode(FriendlyByteBuf buffer);

	Class<T> messageClass();
	//?}
}
