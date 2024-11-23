package me.unclickable.mgui.bukkit.utils.items;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class NamespacedKeyData<T> {

    private final NamespacedKey key;
    private final PersistentDataType<?, T> type;
    private final T value;

    public NamespacedKeyData(NamespacedKey key, PersistentDataType<?, T> type, T value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

}
