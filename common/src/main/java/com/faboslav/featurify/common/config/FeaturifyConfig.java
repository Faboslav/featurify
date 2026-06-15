package com.faboslav.featurify.common.config;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.config.data.*;
import com.faboslav.featurify.common.config.data.serialization.PlacedFeatureDataSerializer;
import com.faboslav.featurify.common.events.common.UpdateRegistriesEvent;
import com.faboslav.featurify.common.platform.PlatformHooks;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class FeaturifyConfig
{
	private static final Path BACKUP_CONFIG_DIR = Path.of("config/featurify");
	private static final String BACKUP_PREFIX = Featurify.MOD_ID + "_backup_";
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

	public boolean isLoaded = false;
	public boolean isLoading = false;

	private final Path configPath = Path.of("config", Featurify.MOD_ID + ".json");
	public final Path configDumpPath = Path.of("config", Featurify.MOD_ID + "_dump.json");
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public boolean disableAllPlacedFeatures = false;
	private Map<String, PlacedFeatureData> placedFeatureData = new TreeMap<>();

	private static final String CONFIG_VERSION_PROPERTY = "config_version";
	private static final String CONFIG_DATETIME_PROPERTY = "config_datetime";
	private static final String GLOBAL_PROPERTY = "global";
	private static final String DISABLE_ALL_PLACED_FEATURES_PROPERTY = "disable_all_placed_features";

	private static final String PLACED_FEATURES_PROPERTY = "placed_features";

	public Map<String, PlacedFeatureData> getPlacedFeatureData() {
		return this.placedFeatureData;
	}

	public void create() {
		if (Files.exists(configPath)) {
			return;
		}

		this.save();
	}

	public void load() {
		if (this.isLoading) {
			return;
		}

		try {
			Featurify.getLogger().info("Loading Featurify config...");
			this.isLoading = true;

			WorldgenDataProvider.loadWorldgenData();
			this.placedFeatureData = WorldgenDataProvider.getPlacedFeatures();

			if (!Files.exists(configPath)) {
				return;
			}

			String jsonString = Files.readString(configPath);
			JsonObject json = gson.fromJson(jsonString, JsonObject.class);

			loadGlobal(json);
			loadPlacedFeatures(json);

			Featurify.getLogger().info("Featurify config loaded");
			this.isLoaded = true;
		} catch (Exception e) {
			Featurify.getLogger().error("Failed to load Featurify config");
			e.printStackTrace();
		} finally {
			this.isLoading = false;
		}
	}

	private void loadGlobal(JsonObject json) {
		if (!json.has(GLOBAL_PROPERTY)) {
			return;
		}

		var global = json.getAsJsonObject(GLOBAL_PROPERTY);

		if (global.has(DISABLE_ALL_PLACED_FEATURES_PROPERTY)) {
			this.disableAllPlacedFeatures = global.get(DISABLE_ALL_PLACED_FEATURES_PROPERTY).getAsBoolean();
		}
	}

	private void loadPlacedFeatures(JsonObject json) {
		if (!json.has(PLACED_FEATURES_PROPERTY)) {
			return;
		}

		var placedFeatures = json.getAsJsonArray(PLACED_FEATURES_PROPERTY);

		for (JsonElement placedFeature : placedFeatures) {
			var placedFeatureJson = placedFeature.getAsJsonObject();

			if (!placedFeatureJson.has(PlacedFeatureDataSerializer.NAME_PROPERTY)) {
				Featurify.getLogger().info("Found invalid placed feature entry, skipping.");
				continue;
			}

			if (!this.placedFeatureData.containsKey(placedFeatureJson.get(PlacedFeatureDataSerializer.NAME_PROPERTY).getAsString())) {
				Featurify.getLogger().info("Found invalid placed feature identifier of \"{}\", skipping.", placedFeatureJson.get(PlacedFeatureDataSerializer.NAME_PROPERTY).getAsString());
				continue;
			}

			PlacedFeatureData placedFeatureData = this.placedFeatureData.get(placedFeatureJson.get(PlacedFeatureDataSerializer.NAME_PROPERTY).getAsString());

			if (placedFeatureData == null) {
				continue;
			}

			PlacedFeatureDataSerializer.load(placedFeatureJson, placedFeatureData);
		}
	}

	public void save() {
		this.save(true);
	}

	public void save(boolean syncRegistries) {
		Featurify.getLogger().info("Saving Featurify config...");

		try {
			if (Files.exists(configPath)) {
				Path backupConfigPath = this.getBackupConfigPath();

				if (!Files.exists(BACKUP_CONFIG_DIR) || !Files.isDirectory(BACKUP_CONFIG_DIR)) {
					Files.createDirectories(BACKUP_CONFIG_DIR);
				}

				if (!Files.exists(backupConfigPath)) {
					Files.move(configPath, backupConfigPath);
					pruneBackupConfigFiles(5);
				}
			}

			JsonObject json = new JsonObject();

			json.addProperty(CONFIG_VERSION_PROPERTY, PlatformHooks.PLATFORM_HELPER.getModVersion());
			json.addProperty(CONFIG_DATETIME_PROPERTY, LocalDateTime.now().format(DATETIME_FORMATTER));
			this.saveGlobalData(json);
			this.savePlacedFeaturesData(json, true);

			Files.createDirectories(configPath.getParent());
			Files.createFile(configPath);
			Files.writeString(configPath, gson.toJson(json));

			Featurify.getLogger().info("Featurify config saved");

			if(syncRegistries) {
				Featurify.getLogger().info("Syncing changes to registries...");
				UpdateRegistriesEvent.EVENT.invoke(new UpdateRegistriesEvent(RegistryManagerProvider.getRegistryManager()));
				Featurify.getLogger().info("Registries synced");
			}
		} catch (Exception e) {
			Featurify.getLogger().error("Failed to save Featurify config");
			e.printStackTrace();

			try {
				Path possibleLatestBackupConfigPath = this.getLatestBackupConfigPath();

				if (possibleLatestBackupConfigPath != null) {
					Featurify.getLogger().error("Restoring Featurify backup config...");
					if (Files.exists(configPath)) {
						Files.delete(configPath);
					}

					Files.move(possibleLatestBackupConfigPath, configPath);
				}
			} catch (Exception fe) {
				Featurify.getLogger().error("Failed to restore Featurify backup config");
				fe.printStackTrace();
			}
		}
	}

	public void dump() {
		Featurify.getLogger().info("Dumping Featurify config...");

		try {
			if (Files.exists(configDumpPath)) {
				Files.delete(configDumpPath);
			}

			JsonObject json = new JsonObject();

			json.addProperty(CONFIG_VERSION_PROPERTY, PlatformHooks.PLATFORM_HELPER.getModVersion());
			json.addProperty(CONFIG_DATETIME_PROPERTY, LocalDateTime.now().format(DATETIME_FORMATTER));
			this.saveGlobalData(json);
			this.savePlacedFeaturesData(json, false);

			Files.createDirectories(configDumpPath.getParent());
			Files.createFile(configDumpPath);
			Files.writeString(configDumpPath, gson.toJson(json));

			Featurify.getLogger().info("Featurify config successfully dumped");
		} catch (Exception e) {
			Featurify.getLogger().error("Failed to dump Featurify config");
			e.printStackTrace();
		}
	}

	private void saveGlobalData(JsonObject json) {
		JsonObject general = new JsonObject();
		general.addProperty(DISABLE_ALL_PLACED_FEATURES_PROPERTY, this.disableAllPlacedFeatures);

		json.add(GLOBAL_PROPERTY, general);
	}


	private void savePlacedFeaturesData(JsonObject json, boolean saveOnlyChanged) {
		JsonArray placedFeatures = new JsonArray();

		this.placedFeatureData.entrySet().stream()
			.filter(entry -> !saveOnlyChanged || !entry.getValue().isUsingDefaultValues())
			.forEach(placedFeatureDataEntry -> {
				PlacedFeatureDataSerializer.save(placedFeatures, placedFeatureDataEntry.getKey(), placedFeatureDataEntry.getValue());
			});

		json.add(PLACED_FEATURES_PROPERTY, placedFeatures);
	}

	private Path getBackupConfigPath() {
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		return Path.of(BACKUP_CONFIG_DIR.toString(), Featurify.MOD_ID + "_backup_" + dateTime + ".json");
	}

	private Path getLatestBackupConfigPath() {
		try {
			if (!Files.exists(BACKUP_CONFIG_DIR) || !Files.isDirectory(BACKUP_CONFIG_DIR)) {
				return null;
			}

			Optional<Path> latest = Files.list(BACKUP_CONFIG_DIR)
				.filter(path -> path.getFileName().toString().startsWith(BACKUP_PREFIX) && path.toString().endsWith(".json"))
				.max(Comparator.comparing(path -> {
					String timestamp = path.getFileName().toString()
						.replace(BACKUP_PREFIX, "")
						.replace(".json", "");
					try {
						return LocalDateTime.parse(timestamp, DATETIME_FORMATTER);
					} catch (Exception e) {
						return LocalDateTime.MIN;
					}
				}));

			return latest.orElse(null);

		} catch (IOException e) {
			Featurify.getLogger().error("Failed to load Featurify backup configs");
			e.printStackTrace();
			return null;
		}
	}

	private void pruneBackupConfigFiles(int keep) {
		try {
			if (!Files.exists(BACKUP_CONFIG_DIR) || !Files.isDirectory(BACKUP_CONFIG_DIR)) {
				return;
			}

			List<Path> backups = Files.list(BACKUP_CONFIG_DIR)
				.filter(path -> {
					String name = path.getFileName().toString();
					return name.startsWith(BACKUP_PREFIX) && name.endsWith(".json");
				})
				.sorted((a, b) -> {
					String ta = a.getFileName().toString()
						.replace(BACKUP_PREFIX, "")
						.replace(".json", "");
					String tb = b.getFileName().toString()
						.replace(BACKUP_PREFIX, "")
						.replace(".json", "");
					try {
						LocalDateTime da = LocalDateTime.parse(ta, DATETIME_FORMATTER);
						LocalDateTime db = LocalDateTime.parse(tb, DATETIME_FORMATTER);
						return db.compareTo(da);
					} catch (Exception e) {
						return 0;
					}
				})
				.toList();

			if (backups.size() <= keep) {
				return;
			}

			for (int i = keep; i < backups.size(); i++) {
				try {
					Files.deleteIfExists(backups.get(i));
				} catch (IOException ex) {
					Featurify.getLogger().warn("Failed to delete old backup {}", backups.get(i).getFileName().toString());
				}
			}
		} catch (IOException e) {
			Featurify.getLogger().error("Failed to prune Featurify backup configs");
			e.printStackTrace();
		}
	}
}