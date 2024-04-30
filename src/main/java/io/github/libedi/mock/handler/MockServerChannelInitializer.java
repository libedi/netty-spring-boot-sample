package io.github.libedi.mock.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
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
		final int maxFrameLength = 58 + 140; // header + body
		final int lengthFieldOffset = 44;
		final int lengthFieldLength = Integer.BYTES;
		final int lengthAdjustment = 10; // Header.reserved's length is 10 bytes.
		ch.pipeline().addFirst(new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength,
				lengthAdjustment, 0)).addLast(new ByteToMockMessageDecoder());
	}

}
