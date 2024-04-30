package io.github.libedi.mock.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
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

	}

}
