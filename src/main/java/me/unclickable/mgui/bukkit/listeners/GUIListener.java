package me.unclickable.mgui.bukkit.listeners;

import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.gui.GUIManager;
import me.unclickable.mgui.bukkit.load.LoadManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;

        if(GUIManager.getHumanGui().values().stream()
                .anyMatch(guiData -> guiData.getInventory().equals(e.getClickedInventory()))) e.setCancelled(true);

        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        LoadManager.getInstance(GUIManager.class)
                .map(GUIManager::getInstance)
                .ifPresent(guiManager -> guiManager.handleClick(e.getWhoClicked(), e.getCurrentItem()));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(MGUI.getInstance(), () -> GUIManager.getHumanGui().values()
                .removeIf(guiData -> guiData.getInventory().getViewers().isEmpty()), 20L);
    }

}
