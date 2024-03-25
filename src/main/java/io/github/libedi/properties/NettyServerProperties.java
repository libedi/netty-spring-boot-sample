package io.github.libedi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ActiveServerProperties
 *
 * @author libed
 *
 */
@ConfigurationProperties("netty")
public record NettyServerProperties(ActiveServer activeServer, int port) {
}
