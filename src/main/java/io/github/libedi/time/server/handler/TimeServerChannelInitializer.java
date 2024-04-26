package io.github.libedi.time.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * TimeServerChannelInitializer
 *
 * @author libed
 *
 */
public class TimeServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(final SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new TimeServerHandler());
	}

}
