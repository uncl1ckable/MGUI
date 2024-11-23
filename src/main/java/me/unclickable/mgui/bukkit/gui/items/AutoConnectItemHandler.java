package me.unclickable.mgui.bukkit.gui.items;

import me.unclickable.mgui.bukkit.gui.AbstractItemGUIHandler;
import me.unclickable.mgui.bukkit.gui.GUIData;
import me.unclickable.mgui.bukkit.gui.GUIManager;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.others.BungeeManager;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.netty.common.ServerData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class AutoConnectItemHandler extends AbstractItemGUIHandler {

    public AutoConnectItemHandler(String key) {
        super(key);
    }

    @Override
    public void handleClick(HumanEntity humanEntity, ItemStack item) {
        if(!(humanEntity instanceof Player p)) return;

        LoadManager.getInstance(ServerManager.class).ifPresent(serverManager -> {
            GUIData guiData = GUIManager.getHumanGui().get(humanEntity);

            serverManager.getServers(guiData.getCategory()).stream()
                    .filter(serverData -> serverData.getPlayers() != serverData.getMaxPlayers())
                    .max(Comparator.comparing(ServerData::getPlayers))
                    .ifPresent(serverData -> LoadManager.getInstance(BungeeManager.class)
                            .ifPresent(bungeeManager -> bungeeManager.send(p, serverData.getServerKey())));
        });
    }

}
