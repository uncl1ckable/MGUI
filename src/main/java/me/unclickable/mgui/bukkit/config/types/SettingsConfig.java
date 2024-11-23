package me.unclickable.mgui.bukkit.config.types;

import lombok.Builder;

import java.util.Map;

@Builder
public record SettingsConfig(long refresh,
                             Map<String, String> placeholders) {}