package io.github.libedi.mock.server;

import java.util.Arrays;

import io.github.libedi.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * MockServer
 *
 * @author libed
 *
 */
@Slf4j
public class MockServer extends Server {

	public MockServer(final ServerBootstrap serverBootstrap, final int port) {
		super(serverBootstrap, port);
	}

	@Override
	protected void process(final ServerBootstrap serverBootstrap, final int port) throws Exception {
		// 다중 포트 접속을 상정
		Arrays.asList(port).stream()
				.map(serverBootstrap::bind)
				.toList().stream()
				.map(ChannelFuture::syncUninterruptibly)
				.toList().stream()
				.map(f -> f.channel().closeFuture())
				.toList().stream()
				.forEach(f -> {
					try {
						f.sync();
					} catch (final InterruptedException e) {
						log.error(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				});
	}

}
