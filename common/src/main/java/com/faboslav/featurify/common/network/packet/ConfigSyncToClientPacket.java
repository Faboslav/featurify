package com.faboslav.featurify.common.network.packet;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.network.MessageHandler;
import com.faboslav.featurify.common.network.Packet;
import com.faboslav.featurify.common.network.base.ClientboundPacketType;
import com.faboslav.featurify.common.network.base.PacketType;
import com.faboslav.featurify.common.versions.VersionedPlayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;

//? if >= 1.20.2 {
import net.minecraft.network.RegistryFriendlyByteBuf;
 //?} else {
/*import net.minecraft.network.FriendlyByteBuf;
*///?}

public record ConfigSyncToClientPacket(String config, boolean save, UUID playerId) implements Packet<ConfigSyncToClientPacket>
{
	private static final Gson GSON = new Gson();
	public static final Identifier ID = Featurify.makeId("config_sync_to_client_packet");
	public static final ClientboundPacketType<ConfigSyncToClientPacket> TYPE = new Handler();

	public static void sendToClient(FeaturifyConfig config, Player player, boolean save) {
		MessageHandler.DEFAULT_CHANNEL.sendToPlayer(
			new ConfigSyncToClientPacket(
				GSON.toJson(config.toJson(true)),
				save,
				player.getUUID()
			),
			player
		);
	}

	@Override
	public PacketType<ConfigSyncToClientPacket> type() {
		return TYPE;
	}

	public static class Handler implements ClientboundPacketType<ConfigSyncToClientPacket>
	{
		@Override
		public Identifier id() {
			return ID;
		}

		@Override
		public Runnable handle(final ConfigSyncToClientPacket packet) {
			return () -> {
				var player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerId);

				try {
					Featurify.getConfig().loadFromJson(GSON.fromJson(packet.config(), JsonObject.class));

					if (packet.save()) {
						Featurify.getConfig().save();
					}
				} catch (Throwable e) {
					Featurify.getLogger().error("Failed to load config from server.", e);

					if (player != null) {
						VersionedPlayer.sendSystemMessage(player, Component.literal("Failed to sync the Featurify config from the server."));
					}

					return;
				}

				if (packet.save() && player != null) {
					VersionedPlayer.sendSystemMessage(player, Component.literal("Featurify config synced from the server."));
				}
			};
		}

		//? if >= 1.20.2 {
		public ConfigSyncToClientPacket decode(final RegistryFriendlyByteBuf buf) {
			return new ConfigSyncToClientPacket(buf.readUtf(), buf.readBoolean(), buf.readUUID());
		}

		public void encode(final ConfigSyncToClientPacket packet, final RegistryFriendlyByteBuf buf) {
			buf.writeUtf(packet.config());
			buf.writeBoolean(packet.save());
			buf.writeUUID(packet.playerId());
		}
		//?} else {
		/*public ConfigSyncToClientPacket decode(final FriendlyByteBuf buf) {
			return new ConfigSyncToClientPacket(buf.readUtf(), buf.readBoolean(), buf.readUUID());
		}

		public void encode(final ConfigSyncToClientPacket packet, final FriendlyByteBuf buf) {
			buf.writeUtf(packet.config());
			buf.writeBoolean(packet.save());
			buf.writeUUID(packet.playerId());
		}

		@Override
		public Class<ConfigSyncToClientPacket> messageClass() {
			return ConfigSyncToClientPacket.class;
		}
		*///?}
	}
}
