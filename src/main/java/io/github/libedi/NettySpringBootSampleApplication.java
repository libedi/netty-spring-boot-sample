package io.github.libedi;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.github.libedi.discard.server.DiscardServer;
import io.netty.bootstrap.ServerBootstrap;

@SpringBootApplication
public class NettySpringBootSampleApplication {

	public static void main(final String[] args) {
		SpringApplication.run(NettySpringBootSampleApplication.class, args);
	}

    @Bean
    Server server(final ServerBootstrap serverBootstrap) {
        return new DiscardServer(serverBootstrap, 18080);
    }

    @Bean
    ApplicationRunner startUp(final Server server) {
        return args -> server.run();
    }

}
