package io.github.libedi;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.github.libedi.discard.server.DiscardServer;
import io.github.libedi.echo.server.EchoServer;
import io.github.libedi.properties.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;

/**
 * ServerFactoryBean
 *
 * @author libed
 *
 */
@Component
public class ServerFactoryBean implements FactoryBean<Server>, InitializingBean {

    private final NettyServerProperties properties;
    private final ServerBootstrap       discardServerBootstrap;
    private final ServerBootstrap       echoServerBootstrap;

    private Server server;

    ServerFactoryBean(final NettyServerProperties properties,
            @Qualifier("discardServerBootstrap") final ServerBootstrap discardServerBootstrap,
            @Qualifier("echoServerBootstrap") final ServerBootstrap echoServerBootstrap) {
        this.properties = properties;
        this.discardServerBootstrap = discardServerBootstrap;
        this.echoServerBootstrap = echoServerBootstrap;
    }

    @Override
    public Server getObject() throws Exception {
        if (server == null) {
            afterPropertiesSet();
        }
        return server;
    }

    @Override
    public Class<?> getObjectType() {
        return server == null ? Server.class : server.getClass();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final int port = properties.getPort();
        switch (properties.getActiveServer()) {
        case DISCARD:
            server = new DiscardServer(discardServerBootstrap, port);
            break;
        case ECHO:
            server = new EchoServer(echoServerBootstrap, port);
            break;
        default:
            break;
        }
    }

}
