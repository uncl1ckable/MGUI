package me.unclickable.mgui.bukkit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextUtils {

    private static final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();

    public static Component getComponent(String text) {
        return legacyComponentSerializer.deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static String toString(Component component) {
        return legacyComponentSerializer.serialize(component);
    }

}
