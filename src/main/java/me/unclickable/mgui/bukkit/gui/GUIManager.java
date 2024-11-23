package me.unclickable.mgui.bukkit.gui;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.config.ConfigManager;
import me.unclickable.mgui.bukkit.config.types.GUIConfig;
import me.unclickable.mgui.bukkit.gui.items.ServerItemHandler;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.bukkit.utils.CategoryUtils;
import me.unclickable.mgui.bukkit.utils.TextUtils;
import me.unclickable.mgui.bukkit.utils.items.ItemCreator;
import me.unclickable.mgui.bukkit.utils.items.NamespacedKeyData;
import me.unclickable.mgui.netty.common.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class GUIManager implements LoadManagerInterface<GUIManager> {

    @Getter
    private static final Map<HumanEntity, GUIData> humanGui = new HashMap<>();

    private BukkitTask updateInventory;

    @Override
    public GUIManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        updateInventory = Bukkit.getScheduler().runTaskTimer(MGUI.getInstance(), () ->
                humanGui.forEach((entity, guiData) -> updateItems(entity, guiData.getCategory())), 0, 20L);
    }

    public void open(HumanEntity entity, String category) {
        CategoryUtils.getName(category).ifPresent(categoryName -> {
            Inventory inventory = Bukkit.createInventory(null, 54, TextUtils.getComponent(categoryName));
            GUIData guiData = GUIData.builder().category(category).inventory(inventory).page(1).build();
            humanGui.put(entity, guiData);
            updateItems(entity, category);
            entity.openInventory(inventory);
        });
    }

    public void updateItems(HumanEntity entity, String category) {
        updateItems(entity, LoadManager.getInstance(ServerManager.class).map(ServerManager::getInstance).map(sm -> sm.getServers(category)).orElse(Collections.emptyList()));
    }

    public void updateItems(HumanEntity entity, List<ServerData> servers) {
        GUIData guiData = humanGui.get(entity);
        if (guiData == null) return;

        int itemsPerPage = ConfigManager.getConfig().gui().items().get(ItemType.SERVER.toString()).positions().size();
        int totalPages = Math.max(1, (int) Math.ceil((double) servers.size() / itemsPerPage));
        int currentPage = Math.max(1, Math.min(guiData.getPage(), totalPages));
        guiData.setPage(currentPage);

        Inventory inventory = guiData.getInventory();
        inventory.clear();

        List<ItemType> additionalItems = Lists.newArrayList();
        if (servers.isEmpty() || (currentPage > 1 && servers.size() <= (currentPage - 1) * itemsPerPage)) {
            additionalItems.add(ItemType.NON_SERVERS);
        } else {
            if (currentPage > 1) additionalItems.add(ItemType.PREVIOUS_PAGE);
            if (servers.size() > currentPage * itemsPerPage) additionalItems.add(ItemType.NEXT_PAGE);
            additionalItems.add(ItemType.AUTO_CONNECT);
        }

        ConfigManager.getConfig().gui().items().forEach((key, item) -> {
            ItemType itemType = ItemType.valueOf(key);
            Map<Integer, ItemCreator.ItemCreatorBuilder> itemsToCreate = new HashMap<>();

            if (itemType == ItemType.SERVER && !servers.isEmpty()) {
                itemsToCreate.putAll(item.positions().stream()
                        .filter(pos -> (currentPage - 1) * itemsPerPage + item.positions().indexOf(pos) < servers.size())
                        .collect(Collectors.toMap(pos -> pos, pos -> {
                            int serverIndex = (currentPage - 1) * itemsPerPage + item.positions().indexOf(pos);
                            ServerData server = servers.get(serverIndex);

                            ServerItemHandler serverItemHandler = (ServerItemHandler) itemType.getHandler();

                            Map<String, String> replacements = new HashMap<>(Map.of(
                                    "{index}", String.valueOf(serverIndex + 1),
                                    "{players}", String.valueOf(server.getPlayers()),
                                    "{max-players}", String.valueOf(server.getMaxPlayers()),
                                    "{version}", server.getVersion()
                            ));

                            replacements.putAll(server.getPlaceholders());

                            return serverItemHandler.create(
                                    item,
                                    replacements,
                                    item.placeholderLines(),
                                    Set.of(new NamespacedKeyData<>(serverItemHandler.getNamespacedServerKey(), PersistentDataType.STRING, server.getServerKey()))
                            );
                        })));
            } else if (additionalItems.contains(itemType)) {
                itemsToCreate.putAll(createDefaultItems(item, itemType, Map.of(
                        "{next-page}", String.valueOf(currentPage + 1),
                        "{previous-page}", String.valueOf(currentPage - 1)
                )));
            }

            itemsToCreate.forEach((pos, builder) -> inventory.setItem(pos, builder.build().create()));
        });
    }

    private Map<Integer, ItemCreator.ItemCreatorBuilder> createDefaultItems(GUIConfig.Item item, ItemType itemType, Map<String, String> replacements) {
        return item.positions().stream().collect(Collectors.toMap(pos -> pos, pos -> itemType.getHandler().create(item, replacements, Map.of(), Set.of())));
    }

    public void handleClick(@NotNull HumanEntity entity, @NotNull ItemStack clickedItem) {
        Optional.ofNullable(humanGui.get(entity))
                .flatMap(gui -> Arrays.stream(ItemType.values())
                        .map(ItemType::getHandler)
                        .filter(handler -> handler.isItem(clickedItem))
                        .findFirst())
                .ifPresent(handler -> handler.handleClick(entity, clickedItem));
    }

    @Override
    public void terminate() {
        updateInventory.cancel();
        humanGui.values().forEach(gui -> gui.getInventory().getViewers().forEach(HumanEntity::closeInventory));
    }

}
