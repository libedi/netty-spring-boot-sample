package io.github.libedi.mock.handler;

import io.github.libedi.mock.domain.Header;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * MockServerChannelInitializer
 *
 * @author libed
 *
 */
public class MockServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final EventExecutorGroup eventExecutorGroup;

    public MockServerChannelInitializer(final EventExecutorGroup eventExecutorGroup) {
        this.eventExecutorGroup = eventExecutorGroup;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        final int maxFrameLength      = Header.LENGTH + 140; // header + body
        final int lengthFieldOffset   = 44;
        final int lengthFieldLength   = Integer.BYTES;
        final int lengthAdjustment    = 10;                  // Header.reserved's length is 10 bytes.
        final int initialBytesToStrip = 0;                   // do not strip header
        ch.pipeline()
                .addFirst(new LoggingHandler(LogLevel.INFO))
                .addLast(new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                        initialBytesToStrip))
                .addLast(new ByteToMockMessageDecoder())
                .addLast(new MockMessageToByteEncoder())
                .addLast(eventExecutorGroup, new SendRequestInboundHandler())
                .addLast(new ResultResponseInboundHandler())
                .addLast(new OtherRequestInboundHandler());
    }

}
