package io.github.libedi.time.client.handler;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * TimeClientHandler
 *
 * @author libed
 *
 */
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		final ByteBuf m = (ByteBuf) msg;
		try {
			final long currentTimeMills = (m.readUnsignedInt() - 2208988800L) * 1000L;
			log.debug("{}", new Date(currentTimeMills));
			ctx.close();
		} finally {
			m.release();
		}
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		ctx.close();
	}

}
