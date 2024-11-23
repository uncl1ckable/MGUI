package me.unclickable.mgui.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.unclickable.mgui.netty.common.Command;

public abstract class AbstractNettyHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        Command command = Command.deserialize(msg);
        handleCommand(ctx, command);
    }

    protected abstract void handleCommand(ChannelHandlerContext ctx, Command command);

    protected void sendCommand(ChannelHandlerContext ctx, CommandType type, String payload) {
        if(ctx == null) return;

        ctx.writeAndFlush(Command.builder()
                .type(type)
                .payload(payload)
                .build()
                .serialize() + "\n");
    }

}
