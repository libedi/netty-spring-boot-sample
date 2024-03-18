package io.github.libedi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import lombok.Getter;

/**
 * ActiveServerProperties
 *
 * @author libed
 *
 */
@ConfigurationProperties("netty")
@Getter
public class NettyServerProperties {

    private final ActiveServer activeServer;
    private final int          port;

    @ConstructorBinding
    NettyServerProperties(final ActiveServer activeServer, final int port) {
        this.activeServer = activeServer;
        this.port = port;
    }

}
