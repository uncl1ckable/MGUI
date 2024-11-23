package me.unclickable.mgui.proxy.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.unclickable.mgui.netty.NettyServerManager;
import me.unclickable.mgui.proxy.MGUIBungee;
import me.unclickable.mgui.proxy.MGUIVelocity;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerUtils {

    public static Optional<String> getServerName(int port) {
        switch (NettyServerManager.getProxyLoadedType()) {
            case BUNGEE -> {
                return MGUIBungee.getInstance().getServers().values().stream()
                        .filter(server -> ((InetSocketAddress) server.getSocketAddress()).getPort() == port)
                        .map(ServerInfo::getName)
                        .findFirst();
            }
            case VELOCITY -> {
                return MGUIVelocity.getInstance().getAllServers().stream()
                        .filter(registeredServer -> registeredServer.getServerInfo().getAddress().getPort() == port)
                        .map(registeredServer -> registeredServer.getServerInfo().getName())
                        .findFirst();
            }
            default -> {
                return Optional.empty();
            }
        }
    }

}
