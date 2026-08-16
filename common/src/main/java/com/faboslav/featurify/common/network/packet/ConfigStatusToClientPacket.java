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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import java.util.UUID;

//? if >= 1.20.2 {
import net.minecraft.network.RegistryFriendlyByteBuf;
 //?} else {
/*import net.minecraft.network.FriendlyByteBuf;
*///?}

public record ConfigStatusToClientPacket(String config, UUID playerId) implements Packet<ConfigStatusToClientPacket>
{
	private static final Gson GSON = new Gson();
	public static final Identifier ID = Featurify.makeId("config_status_to_client_packet");
	public static final ClientboundPacketType<ConfigStatusToClientPacket> TYPE = new Handler();

	public static void sendToClient(FeaturifyConfig config, Player player) {
		MessageHandler.DEFAULT_CHANNEL.sendToPlayer(
			new ConfigStatusToClientPacket(
				GSON.toJson(config.toJson(true)),
				player.getUUID()
			),
			player
		);
	}

	@Override
	public PacketType<ConfigStatusToClientPacket> type() {
		return TYPE;
	}

	public static class Handler implements ClientboundPacketType<ConfigStatusToClientPacket>
	{
		@Override
		public Identifier id() {
			return ID;
		}

		@Override
		public Runnable handle(
			final ConfigStatusToClientPacket packet
		) {
			return () -> {
				JsonObject serverConfigJson;
				var player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerId);

				try {
					serverConfigJson = GSON.fromJson(packet.config(), JsonObject.class);
				} catch (Throwable e) {
					Featurify.getLogger().error("Failed to read config status from server.", e);

					VersionedPlayer.sendSystemMessage(
						player,
						Component.literal("Failed to check the Featurify config status.")
					);

					return;
				}

				VersionedPlayer.sendSystemMessage(
					player,
					describeConfigStatus(
						Featurify.getConfig(),
						serverConfigJson
					)
				);
			};
		}

		//? if >= 1.20.2 {
		public ConfigStatusToClientPacket decode(final RegistryFriendlyByteBuf buf) {
			return new ConfigStatusToClientPacket(buf.readUtf(), buf.readUUID());
		}

		public void encode(
			final ConfigStatusToClientPacket packet,
			final RegistryFriendlyByteBuf buf
		) {
			buf.writeUtf(packet.config());
			buf.writeUUID(packet.playerId());
		}
		//?} else {
		/*public ConfigStatusToClientPacket decode(final FriendlyByteBuf buf) {
			return new ConfigStatusToClientPacket(buf.readUtf(), buf.readUUID());
		}

		public void encode(
			final ConfigStatusToClientPacket packet,
			final FriendlyByteBuf buf
		) {
			buf.writeUtf(packet.config());
			buf.writeUUID(packet.playerId());
		}

		@Override
		public Class<ConfigStatusToClientPacket> messageClass() {
			return ConfigStatusToClientPacket.class;
		}
		*///?}
	}

	private static Component describeConfigStatus(
		FeaturifyConfig localConfig,
		JsonObject serverConfigJson
	) {
		String localHash = localConfig.computeConfigHash();
		String serverHash = FeaturifyConfig.hashConfigJson(serverConfigJson);

		boolean isSynchronized = localHash.equals(serverHash);

		MutableComponent message = isSynchronized
			? Component.literal("Featurify config is synchronized with the server.")
			.withStyle(ChatFormatting.GREEN)
			: Component.literal("Featurify config differs from the server.")
			.withStyle(ChatFormatting.RED);

		String localHashShort = localHash.substring(0, Math.min(16, localHash.length()));
		String serverHashShort = serverHash.substring(0, Math.min(16, serverHash.length()));

		message.append(
			Component.literal("\nLocal version: " + localHashShort)
				.withStyle(ChatFormatting.GRAY)
		);

		message.append(
			Component.literal("\nServer version: " + serverHashShort)
				.withStyle(ChatFormatting.GRAY)
		);

		return message;
	}
}
