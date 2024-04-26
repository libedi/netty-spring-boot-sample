package io.github.libedi.time.server;

import io.github.libedi.Server;
import io.netty.bootstrap.ServerBootstrap;

/**
 * TimeServer
 *
 * @author libed
 *
 */
public class TimeServer extends Server {

	public TimeServer(final ServerBootstrap serverBootstrap, final int port) {
		super(serverBootstrap, port);
	}

	@Override
	protected void process(final ServerBootstrap serverBootstrap, final int port) throws Exception {
		serverBootstrap.bind(port).sync().channel().closeFuture().sync();
	}

}
