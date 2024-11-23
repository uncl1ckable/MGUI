package me.unclickable.mgui.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.unclickable.mgui.bukkit.gui.items.AutoConnectItemHandler;
import me.unclickable.mgui.bukkit.gui.items.NextPageItemHandler;
import me.unclickable.mgui.bukkit.gui.items.PreviousPageItemHandler;
import me.unclickable.mgui.bukkit.gui.items.ServerItemHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum ItemType {
    SERVER(new ServerItemHandler("server")),
    NON_SERVERS(new AbstractItemGUIHandler("non_servers") {
        @Override
        public void handleClick(HumanEntity humanEntity, ItemStack item) {
            // NON CLICK
        }
    }),
    AUTO_CONNECT(new AutoConnectItemHandler("auto_connect")),
    NEXT_PAGE(new NextPageItemHandler("next_page")),
    PREVIOUS_PAGE(new PreviousPageItemHandler("previous_page"));

    private final AbstractItemGUIHandler handler;
}
