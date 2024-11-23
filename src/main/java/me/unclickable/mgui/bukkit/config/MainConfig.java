package me.unclickable.mgui.bukkit.config;

import lombok.Builder;
import me.unclickable.mgui.bukkit.config.types.ConfigureServersConfig;
import me.unclickable.mgui.bukkit.config.types.GUIConfig;
import me.unclickable.mgui.bukkit.config.types.SettingsConfig;

@Builder
public record MainConfig(ConfigPaths configPaths,
                         SettingsConfig settings,
                         ConfigureServersConfig configureServers,
                         GUIConfig gui) {

    @Builder
    public record ConfigPaths(String settings,
                              String gui,
                              String configureServers) {}

}
