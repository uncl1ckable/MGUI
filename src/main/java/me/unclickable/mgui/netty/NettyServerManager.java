package me.unclickable.mgui.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.unclickable.mgui.netty.common.NettyUtils;
import me.unclickable.mgui.netty.handlers.ServerNettyHandler;
import me.unclickable.mgui.proxy.ProxyLoadedType;

@RequiredArgsConstructor
@Slf4j(topic = "NettyServerManager")
public class NettyServerManager {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    @Setter
    @Getter
    private static ProxyLoadedType proxyLoadedType;

    public void start(ProxyLoadedType proxyLoadedType) {
        setProxyLoadedType(proxyLoadedType);

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        NettyUtils.configureStringPipeline(ch.pipeline());
                        ch.pipeline().addLast(new ServerNettyHandler());
                    }
                });

        try {
            serverChannel = bootstrap.bind(NettyParameters.SERVER_PORT).sync().channel();
            log.info("Netty server started on port: {}", NettyParameters.SERVER_PORT);
        } catch (InterruptedException e) {
            log.error("Failed to start Netty server: {}", e.getMessage());
            Thread.currentThread().interrupt();  // Reinitialize the interrupt
        }
    }

    public void stop() {
        try {
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close().sync();  // Closing the channel
                log.info("Netty server channel closed.");
            }
        } catch (InterruptedException e) {
            log.error("Failed to close Netty server channel: {}", e.getMessage());
            Thread.currentThread().interrupt();  // Reinitialize the interrupt
        } finally {
            try {
                if (bossGroup != null) {
                    bossGroup.shutdownGracefully().sync(); // Completing bossGroup
                }
                if (workerGroup != null) {
                    workerGroup.shutdownGracefully().sync(); // Completing workerGroup
                }
            } catch (InterruptedException e) {
                log.error("Failed to shut down Netty server groups: {}", e.getMessage());
                Thread.currentThread().interrupt();  // Reinitialize the interrupt
            }
        }
    }

}
