package com.faboslav.featurify.common.network.packet;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.network.MessageHandler;
import com.faboslav.featurify.common.network.Packet;
import com.faboslav.featurify.common.network.base.ClientboundPacketType;
import com.faboslav.featurify.common.network.base.PacketType;
//? if >= 1.20.2 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
 *///?} else {
import net.minecraft.network.FriendlyByteBuf;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ConfigSyncRequestToClientPacket() implements Packet<ConfigSyncRequestToClientPacket>
{
	public static final ResourceLocation ID = Featurify.makeId("config_sync_request_to_client_packet");
	public static final ClientboundPacketType<ConfigSyncRequestToClientPacket> TYPE = new Handler();

	public static void sendToClient(Player player) {
		MessageHandler.DEFAULT_CHANNEL.sendToPlayer(new ConfigSyncRequestToClientPacket(), player);
	}

	@Override
	public PacketType<ConfigSyncRequestToClientPacket> type() {
		return TYPE;
	}

	public static class Handler implements ClientboundPacketType<ConfigSyncRequestToClientPacket>
	{
		@Override
		public ResourceLocation id() {
			return ID;
		}

		@Override
		public Runnable handle(final ConfigSyncRequestToClientPacket packet) {
			return () -> ConfigSyncToServerPacket.sendToServer(Featurify.getConfig());
		}

		//? if >= 1.20.2 {
		/*public ConfigSyncRequestToClientPacket decode(final RegistryFriendlyByteBuf buf) {
			return new ConfigSyncRequestToClientPacket();
		}

		public void encode(final ConfigSyncRequestToClientPacket packet, final RegistryFriendlyByteBuf buf) {
		}
		*///?} else {
		public ConfigSyncRequestToClientPacket decode(final FriendlyByteBuf buf) {
			return new ConfigSyncRequestToClientPacket();
		}

		public void encode(final ConfigSyncRequestToClientPacket packet, final FriendlyByteBuf buf) {
		}

		@Override
		public Class<ConfigSyncRequestToClientPacket> messageClass() {
			return ConfigSyncRequestToClientPacket.class;
		}
		//?}
	}
}
