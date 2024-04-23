package io.github.libedi.discard.server;

import io.github.libedi.Server;
import io.netty.bootstrap.ServerBootstrap;

/**
 * DiscardServer
 *
 * @author libed
 *
 */
public class DiscardServer extends Server {

    public DiscardServer(final ServerBootstrap serverBootstrap, final int port) {
		super(serverBootstrap, port);
    }

	@Override
	protected void process(final ServerBootstrap serverBootstrap, final int port) throws Exception {
		serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
	}

}
