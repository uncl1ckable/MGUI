package me.unclickable.mgui.bukkit.gui.items;

import me.unclickable.mgui.bukkit.gui.AbstractItemGUIHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class NextPageItemHandler extends AbstractItemGUIHandler {

    public NextPageItemHandler(String key) {
        super(key);
    }

    @Override
    public void handleClick(HumanEntity humanEntity, ItemStack item) {
        changePage(humanEntity, true);
    }

}
