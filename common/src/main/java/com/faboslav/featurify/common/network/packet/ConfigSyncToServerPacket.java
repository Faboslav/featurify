package com.faboslav.featurify.common.network.packet;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.network.MessageHandler;
import com.faboslav.featurify.common.network.Packet;
import com.faboslav.featurify.common.network.base.PacketType;
import com.faboslav.featurify.common.network.base.ServerboundPacketType;
import com.faboslav.featurify.common.versions.VersionedPlayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
//? if >= 1.20.2 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
 *///?} else {
import net.minecraft.network.FriendlyByteBuf;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record ConfigSyncToServerPacket(String config) implements Packet<ConfigSyncToServerPacket>
{
	private static final Gson GSON = new Gson();
	public static final ResourceLocation ID = Featurify.makeId("config_sync_to_server_packet");
	public static final ServerboundPacketType<ConfigSyncToServerPacket> TYPE = new Handler();

	public static void sendToServer(FeaturifyConfig config) {
		MessageHandler.DEFAULT_CHANNEL.sendToServer(new ConfigSyncToServerPacket(GSON.toJson(config.toJson(true))));
	}

	@Override
	public PacketType<ConfigSyncToServerPacket> type() {
		return TYPE;
	}

	public static class Handler implements ServerboundPacketType<ConfigSyncToServerPacket>
	{
		@Override
		public ResourceLocation id() {
			return ID;
		}

		@Override
		public Consumer<Player> handle(final ConfigSyncToServerPacket packet) {
			return (player) -> {
				try {
					Featurify.getConfig().loadFromJson(GSON.fromJson(packet.config(), JsonObject.class));
					Featurify.getConfig().save();
				} catch (Throwable e) {
					Featurify.getLogger().error("Failed to load config to server.", e);
					VersionedPlayer.sendSystemMessage(player, Component.literal("Failed to sync the Featurify config to the server."));
					return;
				}

				VersionedPlayer.sendSystemMessage(player, Component.literal("Featurify config synced to the server."));
			};
		}

		//? if >= 1.20.2 {
		/*public ConfigSyncToServerPacket decode(final RegistryFriendlyByteBuf buf) {
			return new ConfigSyncToServerPacket(buf.readUtf());
		}

		public void encode(final ConfigSyncToServerPacket packet, final RegistryFriendlyByteBuf buf) {
			buf.writeUtf(packet.config());
		}
		*///?} else {
		public ConfigSyncToServerPacket decode(final FriendlyByteBuf buf) {
			return new ConfigSyncToServerPacket(buf.readUtf());
		}

		public void encode(final ConfigSyncToServerPacket packet, final FriendlyByteBuf buf) {
			buf.writeUtf(packet.config());
		}

		@Override
		public Class<ConfigSyncToServerPacket> messageClass() {
			return ConfigSyncToServerPacket.class;
		}
		//?}
	}
}
