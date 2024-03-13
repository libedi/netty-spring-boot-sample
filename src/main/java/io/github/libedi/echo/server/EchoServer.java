package io.github.libedi.echo.server;

import io.github.libedi.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

/**
 * EchoServer
 *
 * @author libed
 *
 */
@Slf4j
public class EchoServer implements Server {

    private final ServerBootstrap serverBootstrap;
    private final int             port;

    private Channel serverChannel;

    public EchoServer(final ServerBootstrap serverBootstrap, final int port) {
        this.serverBootstrap = serverBootstrap;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            final ChannelFuture serverChannelFuture = serverBootstrap.bind("127.0.0.1", port).sync();
            serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
        } catch (final InterruptedException e) {
            close();
        }
        log.warn("Exit Program.");
    }

    @PreDestroy
    @Override
    public void close() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel.parent().close();
            log.info("CLOSE ServerChannel.");
        }
    }

}
