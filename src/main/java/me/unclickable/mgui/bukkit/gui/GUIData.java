package me.unclickable.mgui.bukkit.gui;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.Inventory;

@Builder
@Data
public class GUIData {

    private final String category;
    private final Inventory inventory;
    private int page;

}
