package me.unclickable.mgui.bukkit.load;

import me.unclickable.mgui.bukkit.commands.CommandManager;
import me.unclickable.mgui.bukkit.config.ConfigManager;
import me.unclickable.mgui.bukkit.gui.GUIManager;
import me.unclickable.mgui.bukkit.listeners.ListenerManager;
import me.unclickable.mgui.bukkit.others.BungeeManager;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.netty.NettyClientManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoadManager {

    private static final List<LoadManagerInterface<?>> loadManagerInterfaces = new ArrayList<>();

    public LoadManager() {
        loadManagerInterfaces.add(new ConfigManager());

        loadManagerInterfaces.add(new NettyClientManager());

        loadManagerInterfaces.add(new ServerManager());

        loadManagerInterfaces.add(new BungeeManager());
        loadManagerInterfaces.add(new ListenerManager());
        loadManagerInterfaces.add(new GUIManager());
        loadManagerInterfaces.add(new CommandManager());
    }

    public void initialize() { loadManagerInterfaces.forEach(LoadManagerInterface::initialize); }

    public void terminate() { loadManagerInterfaces.forEach(LoadManagerInterface::terminate); }

    public void reload() { loadManagerInterfaces.forEach(LoadManagerInterface::reload); }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getInstance(Class<T> clazz) {
        return loadManagerInterfaces.stream()
                .filter(clazz::isInstance)
                .map(manager -> (LoadManagerInterface<T>) manager)
                .map(LoadManagerInterface::getInstance)
                .filter(Objects::nonNull)
                .findFirst();
    }
}