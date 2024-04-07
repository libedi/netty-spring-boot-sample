package io.github.libedi.echo.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.libedi.echo.client.handler.EchoClientChannelInitializer;
import io.github.libedi.echo.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * EchoClientConfiguration
 *
 * @author libed
 *
 */
@Configuration
public class EchoClientConfiguration {

    @Bean(destroyMethod = "shutdownGracefully")
    EventLoopGroup echoClientEventLoopGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    EchoClientChannelInitializer echoClientChannelInitializer(final EchoClientHandler echoClientHandler) {
        return new EchoClientChannelInitializer(echoClientHandler);
    }

    @Bean
    Bootstrap echoClientBootstrap(final EchoClientChannelInitializer echoClientChannelInitializer) {
        return new Bootstrap()
                .group(echoClientEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(echoClientChannelInitializer);
    }
}
