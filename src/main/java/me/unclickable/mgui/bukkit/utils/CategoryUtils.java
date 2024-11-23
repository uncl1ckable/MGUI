package me.unclickable.mgui.bukkit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.unclickable.mgui.bukkit.config.ConfigManager;
import me.unclickable.mgui.bukkit.config.types.ConfigureServersConfig;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryUtils {

    public static Optional<String> getName(String category) {
        return ConfigManager.getConfig().configureServers().servers().entrySet().stream()
                .filter(entry -> entry.getKey().equals(category))
                .map(Map.Entry::getValue)
                .map(ConfigureServersConfig.ServerInfo::name)
                .findFirst();
    }

}
