package me.unclickable.mgui.bukkit.gui.items;

import lombok.Getter;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.gui.AbstractItemGUIHandler;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.others.BungeeManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@Getter
public class ServerItemHandler extends AbstractItemGUIHandler {

    private final NamespacedKey namespacedServerKey;

    public ServerItemHandler(String key) {
        super(key);

        this.namespacedServerKey = new NamespacedKey(MGUI.getInstance(), "server-key");
    }

    @Override
    public void handleClick(HumanEntity humanEntity, ItemStack item) {
        String serverKey = item.getItemMeta().getPersistentDataContainer().get(namespacedServerKey, PersistentDataType.STRING);

        if(serverKey == null) return;

        if(humanEntity instanceof Player p) {
            LoadManager.getInstance(BungeeManager.class).ifPresent(bungeeManager -> bungeeManager.send(p, serverKey));
        }
    }

}
