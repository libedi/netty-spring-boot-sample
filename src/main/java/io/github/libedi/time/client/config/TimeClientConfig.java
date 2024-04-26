package io.github.libedi.time.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.libedi.time.client.handler.TimeClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * TimeClientConfig
 *
 * @author libed
 *
 */
@Configuration
public class TimeClientConfig {
	
	@Bean
	EventLoopGroup timeClieEventLoopGroup() {
		return new NioEventLoopGroup();
	}
	
	@Bean
	ChannelInitializer<SocketChannel> timeClientChannelInitializer() {
		return new TimeClientChannelInitializer();
	}

	@Bean
	Bootstrap timeClientBootstrap(final ChannelInitializer<SocketChannel> timeClientChannelInitializer) {
		return new Bootstrap()
				.group(timeClieEventLoopGroup())
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(timeClientChannelInitializer);
	}

}
