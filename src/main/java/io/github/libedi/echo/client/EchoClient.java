package io.github.libedi.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * EchoClient
 *
 * @author libed
 *
 */
@Slf4j
public class EchoClient implements Runnable {

	private final Bootstrap clientBootstrap;
	private final int port;

	public EchoClient(final Bootstrap clientBootstrap, final int port) {
		this.clientBootstrap = clientBootstrap;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			final ChannelFuture channelFuture = clientBootstrap.connect("127.0.0.1", port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (final InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}

}
