package io.github.libedi;

import io.netty.bootstrap.ServerBootstrap;
import lombok.extern.slf4j.Slf4j;

/**
 * Server
 *
 * @author libed
 *
 */
@Slf4j
public abstract class Server implements Runnable {

	private final ServerBootstrap serverBootstrap;
	private final int port;

	protected Server(final ServerBootstrap serverBootstrap, final int port) {
		this.serverBootstrap = serverBootstrap;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			process(serverBootstrap, port);
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			log.warn("Exit Program.");
		}
	}

	protected abstract void process(ServerBootstrap serverBootstrap, int port) throws Exception;

}
