package io.github.libedi.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.libedi.mock.handler.MockServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * MockServerConfig
 *
 * @author libed
 *
 */
@Configuration
class MockServerConfig {
	
	@Bean(destroyMethod = "shutdownGracefully")
	EventExecutorGroup eventExecutorGroup() {
		return new DefaultEventExecutorGroup(8);
	}
	
	@Bean
	ChannelInitializer<SocketChannel> channelInitializer() {
		return new MockServerChannelInitializer(eventExecutorGroup());
	}
	
	@Profile({ "default", "local", "test" })
	@Configuration
	static class LocalConfig {
		
		@Bean(destroyMethod = "shutdownGracefully")
		EventLoopGroup bossGroup() {
			return new NioEventLoopGroup(1);
		}
		
		@Bean(destroyMethod = "shutdownGracefully")
		EventLoopGroup workerGroup() {
			return new NioEventLoopGroup();
		}
		
		@Bean
		ServerBootstrap serverBootstrap(final ChannelInitializer<SocketChannel> channelInitializer) {
			return new ServerBootstrap()
					.group(bossGroup(), workerGroup())
					.channel(NioServerSocketChannel.class)
					.childHandler(channelInitializer)
					.childOption(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
		}
	}
	
	@Profile({ "dev", "prod" })
	@Configuration
	static class ProductionConfig {
		
		@Bean(destroyMethod = "shutdownGracefully")
		EventLoopGroup bossGroup() {
			return new EpollEventLoopGroup(1);
		}
		
		@Bean(destroyMethod = "shutdownGracefully")
		EventLoopGroup workerGroup() {
			return new EpollEventLoopGroup();
		}
		
		@Bean
		ServerBootstrap serverBootstrap(final ChannelInitializer<SocketChannel> channelInitializer) {
			return new ServerBootstrap()
					.group(bossGroup(), workerGroup())
					.channel(EpollServerSocketChannel.class)
					.childHandler(channelInitializer)
					.childOption(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
		}
	}
	
}
