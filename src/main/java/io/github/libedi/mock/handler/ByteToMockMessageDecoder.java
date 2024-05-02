package io.github.libedi.mock.handler;

import java.net.InetSocketAddress;
import java.util.List;

import io.github.libedi.mock.domain.MockMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * ByteToMockMessageDecoder
 *
 * @author libed
 *
 */
@Slf4j
public class ByteToMockMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.isReadable(MockMessage.getTotalLength(in))) {
            final MockMessage message = MockMessage.from(in);
            if (log.isDebugEnabled() && ctx.channel().localAddress() instanceof InetSocketAddress) {
                log.debug("[PORT: {}] {}", ((InetSocketAddress) ctx.channel().localAddress()).getPort(), message);
            }
            out.add(message);
        }
    }

}
