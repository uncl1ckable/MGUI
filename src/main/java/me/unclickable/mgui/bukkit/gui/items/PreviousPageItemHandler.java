package me.unclickable.mgui.bukkit.gui.items;

import me.unclickable.mgui.bukkit.gui.AbstractItemGUIHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class PreviousPageItemHandler extends AbstractItemGUIHandler {

    public PreviousPageItemHandler(String key) {
        super(key);
    }

    @Override
    public void handleClick(HumanEntity humanEntity, ItemStack item) {
        changePage(humanEntity, false);
    }

}
