package me.unclickable.mgui.bukkit.config;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.config.types.ConfigureServersConfig;
import me.unclickable.mgui.bukkit.config.types.GUIConfig;
import me.unclickable.mgui.bukkit.config.types.SettingsConfig;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import me.unclickable.mgui.bukkit.utils.YamlUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class ConfigManager implements LoadManagerInterface<ConfigManager> {

    @Setter @Getter
    private static MainConfig config;

    private final Moshi moshi = new Moshi.Builder().build();

    @Override
    public ConfigManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        try {
            MainConfig mainConfig = loadOrCreate("config.yml", MainConfig.class);

            if(mainConfig == null) {
                throw new IOException("Failed to load configuration");
            }

            // Loading subconfigurations
            SettingsConfig settings = loadOrCreate(mainConfig.configPaths().settings(), SettingsConfig.class);
            ConfigureServersConfig servers = loadOrCreate(mainConfig.configPaths().configureServers(), ConfigureServersConfig.class);
            GUIConfig gui = loadOrCreate(mainConfig.configPaths().gui(), GUIConfig.class);

            setConfig(MainConfig.builder()
                    .settings(settings)
                    .configureServers(servers)
                    .gui(gui)
                    .build());
        } catch (IOException e) {
            log.warn("Failed to load configuration: {}", e.getMessage());
        }
    }

    @Override
    public void reload() {
        initialize();
    }

    private <T> T loadOrCreate(String fileName, Class<T> clazz) throws IOException {
        File file = new File(MGUI.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            saveDefaultFile(fileName, file);
        }

        // Reading a YAML file
        String yamlContent = new String(Files.readAllBytes(file.toPath()));

        // Convert YAML to JSON
        String jsonContent = YamlUtils.convertYamlToJson(yamlContent);

        // Create an adapter
        JsonAdapter<T> adapter = moshi.adapter(clazz);

        // Convert JSON to an object
        try {
            return adapter.fromJson(jsonContent);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed to load configuration: " + e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("ALL")
    private void saveDefaultFile(String resourceName, File target) {
        target.getParentFile().mkdirs();
        MGUI.getInstance().saveResource(resourceName, false);
    }

}
