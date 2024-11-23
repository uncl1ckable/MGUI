package me.unclickable.mgui.proxy;

import lombok.Getter;
import lombok.Setter;
import me.unclickable.mgui.netty.NettyServerManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class MGUIBungee extends Plugin {

    @Setter
    @Getter
    private static ProxyServer instance;
    private final NettyServerManager nettyServerManager = new NettyServerManager();

    @Override
    public void onEnable() {
        setInstance(ProxyServer.getInstance());
        nettyServerManager.start(ProxyLoadedType.BUNGEE);
    }

    @Override
    public void onDisable() {
        nettyServerManager.stop();
    }

}
