package io.github.libedi.mock.handler;

import java.net.InetSocketAddress;
import java.time.Duration;

import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.domain.SendResponseBody;
import io.github.libedi.mock.util.DataConverter;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * SendRequestInboundHandler
 *
 * @author libed
 *
 */
@Slf4j
public class SendRequestInboundHandler extends SimpleChannelInboundHandler<MockMessage> {

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final MockMessage msg) throws Exception {
		if (msg.isSendRequest()) {
			if (log.isDebugEnabled() && ctx.channel().localAddress() instanceof InetSocketAddress) {
				log.debug("[PORT: {}] {}", ((InetSocketAddress) ctx.channel().localAddress()).getPort(), msg);
			}
			final MockMessage sendResponse = DataConverter.convertMessage(msg);
			final ChannelFuture future = ctx.writeAndFlush(sendResponse);
			future.addListener(f -> {
				if (((SendResponseBody) sendResponse.getBody()).isSuccess()) {
					Thread.sleep(Duration.ofMillis(50L));
					ctx.writeAndFlush(DataConverter.convertMessage(sendResponse));
				}
			});
		} else {
			ctx.fireChannelRead(msg);
		}
	}


}
