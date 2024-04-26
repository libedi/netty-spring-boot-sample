package io.github.libedi.time.client;

import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;

/**
 * TimeClient
 *
 * @author libed
 *
 */
@Slf4j
public class TimeClient implements Runnable {

	private final Bootstrap bootstrap;
	private final int port;

	public TimeClient(final Bootstrap bootstrap, final int port) {
		this.bootstrap = bootstrap;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			bootstrap.connect("127.0.0.1", port).sync().channel().closeFuture().sync();
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
