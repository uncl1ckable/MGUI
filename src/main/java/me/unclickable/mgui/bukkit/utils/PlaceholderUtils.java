package me.unclickable.mgui.bukkit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceholderUtils {

    public static Map<String, String> collect(Map<String, String> required) {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return Collections.emptyMap();

        return required.entrySet().stream()
                .filter(entry -> !required.containsValue(
                        PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayers()[0], entry.getValue())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayers()[0], entry.getValue())
                ));
    }

}
