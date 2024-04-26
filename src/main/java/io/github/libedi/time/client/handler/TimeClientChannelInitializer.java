package io.github.libedi.time.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * TimeClientChannelInitializer
 *
 * @author libed
 *
 */
public class TimeClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(final SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new TimeClientHandler());
	}

}
