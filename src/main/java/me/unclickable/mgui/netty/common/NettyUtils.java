package me.unclickable.mgui.netty.common;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyUtils {

    /**
     * Configures the channel pipeline with standard handlers for string-based communication.
     *
     * @param pipeline the pipeline to configure
     */
    public static void configureStringPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new LineBasedFrameDecoder(1024)); // Decodes based on newline
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8)); // Converts ByteBuf to String
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8)); // Converts String to ByteBuf
    }

}
