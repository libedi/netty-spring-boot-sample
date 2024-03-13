package io.github.libedi;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.github.libedi.echo.server.EchoServer;
import io.netty.bootstrap.ServerBootstrap;

@SpringBootApplication
public class NettySpringBootSampleApplication {

	public static void main(final String[] args) {
		SpringApplication.run(NettySpringBootSampleApplication.class, args);
	}

    @Bean
    Server server(@Qualifier("echoServerBootstrap") final ServerBootstrap serverBootstrap) {
//        return new DiscardServer(serverBootstrap, 18080);
        return new EchoServer(serverBootstrap, 18080);
    }

    @Bean
    ApplicationRunner startUp(final Server server) {
        return args -> CompletableFuture.runAsync(() -> server.run());
    }

}
