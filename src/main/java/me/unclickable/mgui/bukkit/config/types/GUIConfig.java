package me.unclickable.mgui.bukkit.config.types;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record GUIConfig(Parameters parameters,
                        Map<String, Item> items) {

    @Builder
    public record Parameters(int size) {}

    @Builder
    public record Item(String name,
                       String material,
                       List<Integer> positions,
                       List<String> lore,
                       Map<String, String> placeholderLines) {}

}
