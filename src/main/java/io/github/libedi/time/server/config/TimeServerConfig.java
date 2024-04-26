package io.github.libedi.time.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.libedi.time.server.handler.TimeServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TimeServerConfig
 *
 * @author libed
 *
 */
@Configuration
class TimeServerConfig {

	@Bean(destroyMethod = "shutdownGracefully")
	EventLoopGroup timeBossGroup() {
		return new NioEventLoopGroup();
	}

	@Bean(destroyMethod = "shutdownGracefully")
	EventLoopGroup timeWorkerGroup() {
		return new NioEventLoopGroup();
	}

	@Bean
	ChannelInitializer<SocketChannel> timeServerChannelInitializer() {
		return new TimeServerChannelInitializer();
	}
	
	@Bean
	ServerBootstrap timeServerBootstrap() {
		return new ServerBootstrap()
				.group(timeBossGroup(), timeWorkerGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(timeServerChannelInitializer())
				.childOption(ChannelOption.SO_BACKLOG, 128);
	}

}
