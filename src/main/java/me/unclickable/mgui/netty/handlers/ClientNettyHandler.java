package me.unclickable.mgui.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.netty.AbstractNettyHandler;
import me.unclickable.mgui.netty.CommandType;
import me.unclickable.mgui.netty.common.Command;
import me.unclickable.mgui.netty.common.ServerData;
import me.unclickable.mgui.netty.common.UpdateInfo;
import me.unclickable.mgui.utils.GsonUtils;

import java.util.List;

@Slf4j
public class ClientNettyHandler extends AbstractNettyHandler {

    private final int serverPort;

    public ClientNettyHandler(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    protected void handleCommand(ChannelHandlerContext ctx, Command command) {
        if (command.getType() == CommandType.INFO) {
            LoadManager.getInstance(ServerManager.class)
                    .map(ServerManager::getInstance)
                    .ifPresent(serverManager -> serverManager.acceptQuery(GsonUtils.deserializeMap(command.getPayload(), String.class, ServerData.class)));
        } else if(command.getType() == CommandType.ERROR) {
            log.error("An error occurred: {}", command.getPayload());
        }
    }

    public void registerServer(ChannelHandlerContext ctx) {
        sendCommand(ctx, CommandType.REGISTER, String.valueOf(serverPort));
    }

    public void updateServerInfo(ChannelHandlerContext ctx, ServerData serverData) {
        UpdateInfo updateInfo = UpdateInfo.builder()
                .port(serverPort)
                .serverData(serverData)
                .build();

        sendCommand(ctx, CommandType.UPDATE_INFO, updateInfo.serialize());
    }

    public void getServerInfo(ChannelHandlerContext ctx, List<String> serverName) {
        sendCommand(ctx, CommandType.GET_INFO, GsonUtils.serialize(serverName));
    }

}
