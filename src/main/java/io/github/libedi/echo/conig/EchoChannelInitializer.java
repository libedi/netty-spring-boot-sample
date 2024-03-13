package io.github.libedi.echo.conig;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * EchoChannelInitializer
 * 
 * - 채널의 요청을 처리하는 파이프라인을 구성하기 위한 ChannelHandler
 * - 보통은 new 로 ChannelHandler를 생성하여 추가하고,
 * - {@link Sharable @Sharable} 애노테이션이 선언된 ChannelHandler만 bean으로 받아 추가한다.
 *
 * @author libed
 *
 */
public class EchoChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelHandler sharableChannelHandler;

    /**
     * EchoChannelInitializer
     *
     * @param sharableChannelHandler {@link Sharable @Sharable} 애노테이션이 붙은 공유 채널 핸들러 빈
     */
    EchoChannelInitializer(final ChannelHandler sharableChannelHandler) {
        this.sharableChannelHandler = sharableChannelHandler;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        ch.pipeline().addLast(sharableChannelHandler);
    }

}
