package me.unclickable.mgui.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.unclickable.mgui.bukkit.MGUI;
import me.unclickable.mgui.bukkit.load.LoadManager;
import me.unclickable.mgui.bukkit.load.LoadManagerInterface;
import me.unclickable.mgui.bukkit.servers.ServerManager;
import me.unclickable.mgui.netty.common.NettyUtils;
import me.unclickable.mgui.netty.handlers.ClientNettyHandler;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
@Slf4j(topic = "NettyClientManager")
public class NettyClientManager implements LoadManagerInterface<NettyClientManager> {

    private static final int RECONNECT_DELAY = 5; // Delay between reconnection attempts in seconds

    private EventLoopGroup group;
    private Channel channel;
    private ClientNettyHandler handler;

    @Override
    public NettyClientManager getInstance() {
        return this;
    }

    @Override
    public void initialize() {
        group = new NioEventLoopGroup();
        Bukkit.getScheduler().runTaskAsynchronously(MGUI.getInstance(), this::connectToServer);
    }

    private void connectToServer() {
        try {
            Bootstrap bootstrap = createBootstrap();
            attemptConnect(bootstrap);
        } catch (Exception e) {
            log.error("Unexpected error during Netty client initialization: {}", e.getMessage(), e);
        }
    }

    private Bootstrap createBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        NettyUtils.configureStringPipeline(ch.pipeline());
                        ch.pipeline().addLast(new ClientNettyHandler(Bukkit.getPort()));
                    }
                });
        return bootstrap;
    }

    private void attemptConnect(Bootstrap bootstrap) {
        log.info("Attempting to connect to Netty server at port: {}", NettyParameters.SERVER_PORT);
        bootstrap.connect("localhost", NettyParameters.SERVER_PORT).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                onConnectSuccess(future);
            } else {
                onConnectFailure(future);
            }
        });
    }

    private void onConnectSuccess(ChannelFuture future) {
        log.info("Successfully connected to Netty server at port: {}", NettyParameters.SERVER_PORT);
        channel = future.channel();
        handler = channel.pipeline().get(ClientNettyHandler.class);

        handler.registerServer(channel.pipeline().context(handler));

        LoadManager.getInstance(ServerManager.class)
                .map(ServerManager::getInstance)
                .ifPresent(ServerManager::updateStats);

        channel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
            log.warn("Connection to server lost. Scheduling reconnect...");
            scheduleReconnect(channel.eventLoop());
        });
    }

    private void onConnectFailure(ChannelFuture future) {
        log.error("Failed to connect to server: {}. Scheduling reconnect...", future.cause().getMessage());
        scheduleReconnect(group.next());
    }

    private void scheduleReconnect(EventLoop eventLoop) {
        eventLoop.schedule(this::connectToServer, RECONNECT_DELAY, TimeUnit.SECONDS);
    }

    @Override
    public void terminate() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close().sync(); // Closing the channel
            }
        } catch (InterruptedException e) {
            log.error("Failed to close channel: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            if (group != null) {
                group.shutdownGracefully(); // Close EventLoopGroup
            }
        }
    }

}
