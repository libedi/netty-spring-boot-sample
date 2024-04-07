package io.github.libedi.echo.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * EchoClientChannelInitializer
 *
 * @author libed
 *
 */
public class EchoClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelHandler sharableChannelHandler;

    public EchoClientChannelInitializer(final ChannelHandler sharableChannelHandler) {
        this.sharableChannelHandler = sharableChannelHandler;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        ch.pipeline().addLast(sharableChannelHandler);
    }

}
