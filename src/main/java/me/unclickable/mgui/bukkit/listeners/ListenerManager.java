package me.unclickable.mgui.bukkit.listeners;

import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import org.bukkit.Bukkit;

public class ListenerManager implements LoadManagerInterface<ListenerManager> {

    @Override
    public ListenerManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(new GUIListener(), MGUI.getInstance());
    }

}
