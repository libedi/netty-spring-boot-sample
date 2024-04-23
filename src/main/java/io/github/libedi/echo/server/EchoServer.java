package io.github.libedi.echo.server;

import io.github.libedi.Server;
import io.netty.bootstrap.ServerBootstrap;

/**
 * EchoServer
 *
 * @author libed
 *
 */
public class EchoServer extends Server {

    public EchoServer(final ServerBootstrap serverBootstrap, final int port) {
		super(serverBootstrap, port);
    }

	@Override
	protected void process(final ServerBootstrap serverBootstrap, final int port) throws Exception {
		serverBootstrap.bind(port).sync().channel().closeFuture().sync();
	}

}
