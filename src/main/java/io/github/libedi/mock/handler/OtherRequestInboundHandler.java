package io.github.libedi.mock.handler;

import java.net.InetSocketAddress;

import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.util.DataConverter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * OtherRequestInboundHandler
 *
 * @author libed
 *
 */
@Slf4j
public class OtherRequestInboundHandler extends SimpleChannelInboundHandler<MockMessage> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MockMessage msg) throws Exception {
        if (log.isDebugEnabled() && ctx.channel().localAddress() instanceof InetSocketAddress) {
            log.debug("[PORT: {}] {}/{}", ((InetSocketAddress) ctx.channel().localAddress()).getPort(), msg.getHeader().getMessageType(),
                    msg.getHeader().getMessageSubType());
        }
        ctx.write(DataConverter.convertMessage(msg));
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
