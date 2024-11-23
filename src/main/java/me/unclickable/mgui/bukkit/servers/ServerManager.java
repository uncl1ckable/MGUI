package me.unclickable.mgui.bukkit.servers;

import lombok.Getter;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.config.ConfigManager;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import me.unclickable.mgui.bukkit.utils.PlaceholderUtils;
import me.unclickable.mgui.netty.NettyClientManager;
import me.unclickable.mgui.netty.common.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.util.*;

public class ServerManager implements LoadManagerInterface<ServerManager> {

    @Getter
    private static final HashMap<ServerData, String> SERVERS = new HashMap<>();

    private Instant updaterNextExecute = Instant.now();
    private BukkitTask updater;

    @Override
    public ServerManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        updater = Bukkit.getScheduler().runTaskTimer(MGUI.getInstance(), () -> {
            if(Instant.now().isAfter(updaterNextExecute)) {
                updateQuery();
                updateStats();

                updaterNextExecute = Instant.now().plusMillis(ConfigManager.getConfig().settings().refresh());
            }
        }, 0, 20L);
    }

    @Override
    public void terminate() {
        updater.cancel();
    }

    public Optional<String> getCategory(String serverName) {
        return ConfigManager.getConfig().configureServers().servers().entrySet().stream()
                .filter(entry -> entry.getValue().servers().contains(serverName))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void updateQuery() {
        getNettyClient().ifPresent(nettyClientManager -> nettyClientManager.getHandler().getServerInfo(
                nettyClientManager.getChannel().pipeline().context(nettyClientManager.getHandler()),
                ConfigManager.getConfig().configureServers().servers().values().stream()
                        .flatMap(serverInfo -> serverInfo.servers().stream())
                        .toList()
        ));
    }

    public void updateStats() {
        getNettyClient().ifPresent(nettyClientManager -> nettyClientManager.getHandler().updateServerInfo(
                nettyClientManager.getChannel().pipeline().context(nettyClientManager.getHandler()),
                ServerData.builder()
                        .players(Bukkit.getOnlinePlayers().size())
                        .maxPlayers(Bukkit.getMaxPlayers())
                        .version(Bukkit.getMinecraftVersion())
                        .placeholders(PlaceholderUtils.collect(ConfigManager.getConfig().settings().placeholders()))
                        .build()
        ));
    }

    private Optional<NettyClientManager> getNettyClient() {
        return LoadManager.getInstance(NettyClientManager.class)
                .map(NettyClientManager::getInstance)
                .filter(nettyClientManager -> nettyClientManager.getChannel() != null)
                .stream().findFirst();
    }

    public List<ServerData> getServers(String category) {
        if (category == null) return Collections.emptyList();

        return SERVERS.entrySet().stream()
                .filter(entry -> category.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(ServerData::getPlayers).reversed())
                .toList();
    }


    public void acceptQuery(Map<String, ServerData> servers) {
        servers.entrySet().stream()
                .filter(Objects::nonNull)
                .forEach(entry -> getCategory(entry.getKey()).ifPresent(category -> {
                    ServerData serverData = entry.getValue();
                    serverData.setServerKey(entry.getKey());
                    SERVERS.put(serverData, category);
                }));

        SERVERS.keySet().removeIf(serverData -> !servers.containsValue(serverData));
    }

}
