package me.unclickable.mgui.bukkit.load;

public interface LoadManagerInterface<T> {
    T getInstance();

    void initialize();
    default void terminate() {}
    default void reload() {}
}
