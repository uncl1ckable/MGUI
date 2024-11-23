package me.unclickable.mgui.bukkit.config.types;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ConfigureServersConfig(Map<String, ServerInfo> servers) {

    public record ServerInfo(String name,
                             List<String> servers) {}

}
