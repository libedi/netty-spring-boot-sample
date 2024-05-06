package io.github.libedi.mock.handler;

import java.net.InetSocketAddress;

import io.github.libedi.mock.domain.MockMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * ResultResponseInboundHandler
 *
 * @author libed
 *
 */
@Slf4j
public class ResultResponseInboundHandler extends SimpleChannelInboundHandler<MockMessage> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MockMessage msg) throws Exception {
        if (msg.isResultResponse()) {
            if (log.isDebugEnabled() && ctx.channel().localAddress() instanceof InetSocketAddress) {
                log.debug("[PORT: {}] {}/{}", ((InetSocketAddress) ctx.channel().localAddress()).getPort(),
                        msg.getHeader().getMessageType(), msg.getHeader().getMessageSubType());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

}
