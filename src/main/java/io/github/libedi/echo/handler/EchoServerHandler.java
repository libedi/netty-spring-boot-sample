package io.github.libedi.echo.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * EchoServerHandler
 * 
 * 아래 로직은 stateless하고, 간단한 테스트 용도라 @Sharable 애노테이션은 생략
 *
 * @author libed
 *
 */
@Sharable
@Component
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 클라이언트로부터 새 데이터가 수신될 때마다 수신된 메시지와 함께 호출된다.
     * 실제로는 아래와 같이 참조 카운트 객체를 해제시켜 주어야 한다.
     * <code>
     * try {
     *  // do something
     * } finally {
     *  ReferenceCountUtil.release(msg);
     * }
     * </code>
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
