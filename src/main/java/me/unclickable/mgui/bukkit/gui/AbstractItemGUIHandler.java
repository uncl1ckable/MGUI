package me.unclickable.mgui.bukkit.gui;

import com.google.common.collect.Sets;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.config.types.GUIConfig;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.bukkit.utils.items.ItemCreator;
import me.unclickable.mgui.bukkit.utils.items.NamespacedKeyData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Set;

public abstract class AbstractItemGUIHandler {

    protected NamespacedKey namespacedKey;
    protected Set<NamespacedKeyData<?>> additionalKeys = Sets.newHashSet();

    protected AbstractItemGUIHandler(String key) {
        this.namespacedKey = new NamespacedKey(MGUI.getInstance(), key);
    }

    protected ItemCreator.ItemCreatorBuilder create(GUIConfig.Item item, Map<String, String> replacements) {
        return create(item, replacements, Map.of(), Set.of());
    }

    protected ItemCreator.ItemCreatorBuilder create(GUIConfig.Item item,
                                                    Map<String, String> replacements,
                                                    Map<String, String> placeholderLines,
                                                    Set<NamespacedKeyData<?>> additionalKeys) {
        this.additionalKeys = Sets.newHashSet(additionalKeys);

        this.additionalKeys.add(new NamespacedKeyData<>(namespacedKey, PersistentDataType.INTEGER, 1));

        return ItemCreator.builder()
                .displayName(item.name())
                .material(Material.matchMaterial(item.material()))
                .lore(item.lore())
                .replacements(replacements)
                .placeholderLines(placeholderLines)
                .namespacedKeys(this.additionalKeys);
    }

    public boolean isItem(ItemStack itemStack) {
        if(!itemStack.hasItemMeta()) return false;
        else return itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.INTEGER);
    }

    protected void changePage(HumanEntity humanEntity, boolean next) {
        GUIData guiData = GUIManager.getHumanGui().get(humanEntity);

        if(guiData != null) {
            LoadManager.getInstance(ServerManager.class).ifPresent(serverManager ->
                    LoadManager.getInstance(GUIManager.class).ifPresent(guiManager -> {
                        guiData.setPage(guiData.getPage() + (next ? 1 : -1));
                        guiManager.updateItems(humanEntity, serverManager.getServers(guiData.getCategory()));
                    }));
        }
    }

    public abstract void handleClick(HumanEntity humanEntity, ItemStack item);

}
