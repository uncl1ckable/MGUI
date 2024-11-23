package me.unclickable.mgui.bukkit;

import lombok.Getter;
import lombok.Setter;
import me.unclickable.mgui.bukkit.load.LoadManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class MGUI extends JavaPlugin {

    @Getter
    @Setter
    private static MGUI instance;
    private final LoadManager loadManager = new LoadManager();

    @Override
    public void onEnable() {
        setInstance(this);
        loadManager.initialize();
    }

    @Override
    public void onDisable() {
        loadManager.terminate();
    }

}
