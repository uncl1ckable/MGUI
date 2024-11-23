package me.unclickable.mgui.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import me.unclickable.mgui.netty.AbstractNettyHandler;
import me.unclickable.mgui.netty.CommandType;
import me.unclickable.mgui.netty.common.Command;
import me.unclickable.mgui.netty.common.ServerData;
import me.unclickable.mgui.netty.common.UpdateInfo;
import me.unclickable.mgui.proxy.utils.ServerUtils;
import me.unclickable.mgui.utils.GsonUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class ServerNettyHandler extends AbstractNettyHandler {

    private static final ConcurrentHashMap<ChannelHandlerContext, Integer> CTX_PORTS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, String> SERVER_REGISTRY = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ServerData> SERVER_STATS = new ConcurrentHashMap<>();

    @Override
    protected void handleCommand(ChannelHandlerContext ctx, Command command) {
        try {
            switch (command.getType()) {
                case REGISTER -> handleRegister(ctx, parsePort(command));
                case UNREGISTER -> handleUnregister(ctx, parsePort(command));
                case UPDATE_INFO -> handleUpdateInfo(ctx, command.getPayload());
                case GET_INFO -> handleGetInfo(ctx, command.getPayload());
                default -> sendError(ctx, "Unknown command: " + command.getType());
            }
        } catch (Exception e) {
            sendError(ctx, "Error processing command: " + e.getMessage());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        Integer port = CTX_PORTS.remove(ctx);
        if (port != null) {
            handleUnregister(ctx, port);
        }
    }

    private void handleRegister(ChannelHandlerContext ctx, int port) {
        ServerUtils.getServerName(port).ifPresentOrElse(serverName -> {
            SERVER_REGISTRY.put(port, serverName);
            CTX_PORTS.put(ctx, port);
            sendCommand(ctx, CommandType.REGISTERED, serverName);
        }, () -> sendError(ctx, "Server not found in the list"));
    }

    private void handleUnregister(ChannelHandlerContext ctx, int port) {
        String serverName = SERVER_REGISTRY.remove(port);
        if (serverName != null) {
            SERVER_STATS.remove(serverName);
            sendCommand(ctx, CommandType.UNREGISTERED, serverName);
        } else {
            sendError(ctx, "Server not registered");
        }
    }

    private void handleUpdateInfo(ChannelHandlerContext ctx, String payload) {
        UpdateInfo updateInfo = UpdateInfo.deserialize(payload);
        String serverName = SERVER_REGISTRY.get(updateInfo.getPort());

        if (serverName != null) {
            SERVER_STATS.put(serverName, updateInfo.getServerData());
            sendCommand(ctx, CommandType.UPDATED, serverName);
        } else {
            sendError(ctx, "Server not registered");
        }
    }

    private void handleGetInfo(ChannelHandlerContext ctx, String serverNamesSerialized) {
        List<String> serverNames = GsonUtils.deserializeList(serverNamesSerialized, String.class);
        Map<String, ServerData> servers = serverNames.stream()
                .filter(SERVER_STATS::containsKey)
                .collect(Collectors.toMap(serverName -> serverName, SERVER_STATS::get));

        if (!servers.isEmpty()) {
            sendCommand(ctx, CommandType.INFO, GsonUtils.serialize(servers));
        } else {
            sendError(ctx, "Servers information not found");
        }
    }

    private void sendError(ChannelHandlerContext ctx, String message) {
        sendCommand(ctx, CommandType.ERROR, message);
    }

    private int parsePort(Command command) {
        try {
            return Integer.parseInt(command.getPayload());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port format: " + command.getPayload());
        }
    }

}
