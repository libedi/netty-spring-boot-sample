package io.github.libedi;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@ConfigurationPropertiesScan("io.github.libedi.properties")
public class NettySpringBootSampleApplication {

	public static void main(final String[] args) {
		SpringApplication.run(NettySpringBootSampleApplication.class, args);
	}

	@Profile("test")
    @Bean
	ApplicationRunner testRunner(final Server server) {
        return args -> CompletableFuture.runAsync(() -> server.run());
    }

	@Profile("!test")
	@Bean
	ApplicationRunner runner(final Server server) {
		return args -> server.run();
	}

}
