package me.unclickable.mgui.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.Setter;
import me.unclickable.mgui.netty.NettyServerManager;

@Plugin(authors = "unclickable", id = "mgui", name = "MGUI", version = "1.0.0")
@Getter
public class MGUIVelocity {

    @Setter
    @Getter
    private static ProxyServer instance;
    private final NettyServerManager nettyServerManager = new NettyServerManager();

    @Inject
    public MGUIVelocity(ProxyServer server) {
        setInstance(server);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        nettyServerManager.start(ProxyLoadedType.VELOCITY);
    }

    @Subscribe
    public void onProxyDisable(ProxyShutdownEvent event) {
        nettyServerManager.stop();
    }

}
